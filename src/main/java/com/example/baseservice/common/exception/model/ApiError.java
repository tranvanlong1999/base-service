package com.example.baseservice.common.exception.model;

import com.example.baseservice.common.json.JsonUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiError {
	private List<MessageObject> messageObjects;
	private List<MessageField> messageFields;
	private String messageCode;
	private HttpStatus status;
	private String error;

	public ApiError(List<MessageField> messageFields, List<MessageObject> messageObjects, ErrorCode status) {
		super();
		this.messageObjects = messageObjects;
		this.messageFields = messageFields;
		this.messageCode = status.getErrorCode();
		this.status = status.getStatus();
		this.error = status.getReasonPhrase();
	}

	public ApiError(HttpStatus status, String messageCode, List<MessageField> messageFields,
					List<MessageObject> messageObjects) {
		super();
		this.messageObjects = messageObjects;
		this.messageFields = messageFields;
		this.messageCode = messageCode;
		this.status = status;
		this.error = "";
	}

	public ApiError(HttpStatus status, String messageCode) {
		super();
		this.messageObjects = new ArrayList<>();
		this.messageFields = new ArrayList<>();
		this.messageCode = messageCode;
		this.status = status;
		this.error = "";
	}

	public ApiError(ErrorCode errorCode, String error) {
		super();
		this.messageObjects = new ArrayList<>();
		this.messageFields = new ArrayList<>();
		this.messageCode = errorCode.getErrorCode();
		this.status = errorCode.getStatus();
		this.error = error;
	}

	public ApiError(HttpStatus status, String messageCode, String error) {
		super();
		this.messageObjects = new ArrayList<>();
		this.messageFields = new ArrayList<>();
		this.messageCode = messageCode;
		this.status = status;
		this.error = error;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
