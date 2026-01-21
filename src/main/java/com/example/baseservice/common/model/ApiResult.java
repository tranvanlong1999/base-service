package com.example.baseservice.common.model;

import com.example.baseservice.common.exception.model.ErrorCode;
import lombok.Data;

@Data
public class ApiResult<T> {

	private String message;

	private T object;

	public ApiResult(String message, T object) {
		this.message = message;
		this.object = object;
	}

	public ApiResult(ErrorCode message, T object) {
		this.message = message.getErrorCode();
		this.object = object;
	}

	public ApiResult() {
	}
}
