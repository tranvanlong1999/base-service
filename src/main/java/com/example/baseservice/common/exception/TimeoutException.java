package com.example.baseservice.common.exception;


import com.example.baseservice.common.exception.model.ErrorCode;
import lombok.Getter;

@Getter
public class TimeoutException extends RuntimeException {

	private ErrorCode errorCode = ErrorCode.STORE_0408;

	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
