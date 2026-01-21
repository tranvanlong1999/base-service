package com.example.baseservice.common.exception;

import com.example.baseservice.common.exception.model.ApiError;
import com.example.baseservice.common.exception.model.ErrorCode;
import com.example.baseservice.common.exception.model.MessageField;
import com.example.baseservice.common.exception.model.MessageObject;
import com.example.baseservice.infrastructure.repository.I18nRepository;
import com.example.baseservice.service.impl.I18nService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionController extends ResponseEntityExceptionHandler {

    private final MessageSource msgSource;
    private final I18nService i18nService;

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        List<MessageField> msgFields = handleErrorFields(ex.getBindingResult().getFieldErrors(), request);
        List<MessageObject> msgObjects = handleErrorObject(ex.getBindingResult().getGlobalErrors(), request);
        ApiError apiError = new ApiError(msgFields, msgObjects, ErrorCode.STORE_0400);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    public List<MessageField> handleErrorFields(List<FieldError> fieldErrors, @NonNull WebRequest request) {
        return fieldErrors.stream()
                .map(error -> {
                    String msgError;
                    try {
                        String messageExpression = error.unwrap(ConstraintViolation.class).getMessageTemplate().trim();
//                                .split("[{}]")[1]; TODO: fix this
                        msgError = i18nService.resolveMessage(messageExpression, request.getLocale()).toString();
                    } catch (Exception e) {
                        msgError = error.getDefaultMessage();
                    }
                    return new MessageField(error.getField(), msgError);
                })
                .collect(Collectors.toList());
    }

    public List<MessageObject> handleErrorObject(List<ObjectError> objectErrors, @NonNull WebRequest request) {
        return objectErrors.stream()
                .map(error -> {
                    String msgError;
                    try {
                        msgError = msgSource.getMessage(ObjectUtils.defaultIfNull(error.getDefaultMessage(), EMPTY), null, request.getLocale());
                    } catch (Exception e) {
                        msgError = error.getDefaultMessage();
                    }
                    return new MessageObject(error.getObjectName(), msgError);
                })
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, @NonNull HttpHeaders
            headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        final ApiError apiError = new ApiError(ErrorCode.STORE_0400, error);
        log.error("handleTypeMismatch: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException
                                                                             ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final String error = ex.getClass().getName() + " :" + ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(ErrorCode.STORE_0400, error);
        log.error("handleMissingServletRequestPart: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException
                                                                                  ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final String error = ex.getClass().getName() + " :" + ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(ErrorCode.STORE_0400, error);
        log.error("handleMissingServletRequestParameter: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                   final @NonNull WebRequest request) {
        final String error = ex.getClass().getName() + " :" + ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
        final ApiError apiError = new ApiError(ErrorCode.STORE_0400, error);
        log.error("handleMethodArgumentTypeMismatch: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            final @NonNull WebRequest request) {
        final List<MessageField> messageFields = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            for (Path.Node path : violation.getPropertyPath()) {
                if (ElementKind.PARAMETER == path.getKind()) {
                    messageFields.add(new MessageField(path.getName(), violation.getMessage()));
                }
            }
        }
        final ApiError apiError = new ApiError(messageFields, new ArrayList<>(), ErrorCode.STORE_0600);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404
    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, @NonNull HttpHeaders
            headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        final ApiError apiError = new ApiError(ErrorCode.STORE_0404, error);
        log.error("handleNoHandlerFoundException: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405
    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException
                                                                                 ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        if (ex.getSupportedHttpMethods() != null) {
            ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        }
        final ApiError apiError = new ApiError(ErrorCode.STORE_0405, builder.toString());
        log.error("handleHttpRequestMethodNotSupported: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415
    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException
                                                                             ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));
        final ApiError apiError = new ApiError(ErrorCode.STORE_0415, builder.toString());
        log.error("handleHttpMediaTypeNotSupported: {}", apiError);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 403
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex,
                                                              final @NonNull WebRequest request) {
        final String message = ex.getMessage();
        final ApiError apiError = new ApiError(ErrorCode.STORE_0403, message);
        log.error("handleAccessDeniedException: {}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final @NonNull WebRequest request) {
        final ApiError apiError = new ApiError(ErrorCode.STORE_0500,
                ex.getLocalizedMessage().substring(0, Math.min(ex.getLocalizedMessage().length(), 100)));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    void handleIllegalArgumentException(NotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @NonNull
    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
                                                                          final @NonNull HttpHeaders headers,
                                                                          @NonNull final HttpStatus status, final @NonNull WebRequest request) {
        final ApiError apiError = new ApiError(ErrorCode.STORE_0400, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
