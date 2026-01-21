package com.example.baseservice.common.exception;


import com.example.baseservice.common.exception.model.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

	private ErrorCode errorCode = ErrorCode.STORE_0400;

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
