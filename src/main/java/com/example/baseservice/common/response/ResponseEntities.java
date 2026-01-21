package com.example.baseservice.common.response;

import com.example.baseservice.common.exception.model.ApiError;
import com.example.baseservice.common.exception.model.ErrorCode;
import com.example.baseservice.common.model.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseEntities {

    public static <T> ResponseEntity<?> createSuccessResponse(HttpStatus status, T data) {
        ApiResult<?> result = new ApiResult<>(ErrorCode.SUCCESS, data);
        return new ResponseEntity<>(result, status);
    }

    private static ResponseEntity<?> createErrorResponse(HttpStatusCode status, final String errorMessage) {
        final ApiError apiError = new ApiError((HttpStatus) status, errorMessage);
        return new ResponseEntity<>(apiError, status);
    }

    public static ResponseEntity<?> createErrorResponse(HttpStatusCode status, Object data) {
        return data instanceof String str ? createErrorResponse(status, str) :
                new ResponseEntity<>(data, status);
    }

    public static ResponseEntity<?> createErrorResponse(final HttpStatusCode status, final String errorMessage, final String responseData) {
        final ApiError apiError = new ApiError((HttpStatus) status, errorMessage, responseData);
        return new ResponseEntity<>(apiError, status);
    }

    public static ResponseEntity<?> createErrorResponse(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}