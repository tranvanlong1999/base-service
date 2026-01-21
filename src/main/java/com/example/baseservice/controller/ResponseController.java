package com.example.baseservice.controller;

import com.example.baseservice.common.CallbackFunction;
import com.example.baseservice.common.ConstantString;
import com.example.baseservice.common.context.DataContextHelper;
import com.example.baseservice.common.exception.BadRequestException;
import com.example.baseservice.common.exception.ForbiddenException;
import com.example.baseservice.common.exception.NotFoundException;
import com.example.baseservice.common.exception.UnauthorizedException;
import com.example.baseservice.common.exception.model.ErrorCode;
import com.example.baseservice.common.response.ResponseEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
public class ResponseController {

    public DeferredResult<ResponseEntity<?>> responseEntityDeferredResult(CallbackFunction<?> callbackFunction) {
        final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>(ConstantString.TIMEOUT_NONBLOCK);
        deferredResult.onTimeout(() -> handleTimeout(deferredResult));
        try {
            deferredResult.setResult(ResponseEntities.createSuccessResponse(HttpStatus.OK, callbackFunction.execute()));
        } catch (Exception e) {
            handleError(e, deferredResult);
        } finally {
            DataContextHelper.clear();
        }
        return deferredResult;
    }

    private void handleTimeout(DeferredResult<ResponseEntity<?>> deferredResult) {
        ResponseEntity<?> errorResponse = ResponseEntities.createErrorResponse(HttpStatus.REQUEST_TIMEOUT, ErrorCode.STORE_0408.getErrorCode());
        deferredResult.setErrorResult(errorResponse);
    }

    private void handleError(Exception e, DeferredResult<ResponseEntity<?>> deferredResult) {
        switch (e) {
            case NotFoundException ex ->
                    handleException(e, HttpStatus.NOT_FOUND, ErrorCode.STORE_0404.getErrorCode(), deferredResult);
            case BadRequestException ex ->
                    handleException(e, HttpStatus.BAD_REQUEST, ErrorCode.STORE_0400.getErrorCode(), deferredResult);
            case UnauthorizedException ex ->
                    handleException(e, HttpStatus.UNAUTHORIZED, ErrorCode.STORE_0401.getErrorCode(), deferredResult);
            case ForbiddenException ex ->
                    handleException(e, HttpStatus.FORBIDDEN, ErrorCode.STORE_0403.getErrorCode(), deferredResult);
            default ->
                    handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.STORE_0500.getErrorCode(), deferredResult);
        }
    }

    private void handleException(Exception e, HttpStatus httpStatus, String errorCode, DeferredResult<ResponseEntity<?>> deferredResult) {
        log.error("An error occurred: ", e);
        ResponseEntity<?> errorResponse = ResponseEntities.createErrorResponse(httpStatus, errorCode, e.getMessage().trim());
        deferredResult.setResult(errorResponse);
    }

}
