package com.example.baseservice.common.exception;

import com.example.baseservice.common.exception.model.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {

	private ErrorCode errorCode = ErrorCode.STORE_0400;

	public NotFoundException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public NotFoundException(String message) {
		super(message);
	}

}
