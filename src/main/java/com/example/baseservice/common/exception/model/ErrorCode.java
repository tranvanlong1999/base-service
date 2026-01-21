package com.example.baseservice.common.exception.model;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	//define custom error code here
	SUCCESS(Constant.STORE_0000, HttpStatus.OK, "Thành công"),
	STORE_0001(Constant.STORE_0001, HttpStatus.BAD_REQUEST, "Trường không được null"),
	STORE_0003(Constant.STORE_0003, HttpStatus.BAD_REQUEST, "Kích thước/ số lượng không đúng"),
	STORE_0004(Constant.STORE_0004, HttpStatus.BAD_REQUEST, "Trường không được để trống"),
	STORE_0007(Constant.STORE_0007, HttpStatus.BAD_REQUEST, "Ngày giờ không đúng định dạng: ${field}"),
	STORE_0008(Constant.STORE_0008, HttpStatus.BAD_REQUEST, "Số điện thoại không đúng định dạng"),
	STORE_0009(Constant.STORE_0009, HttpStatus.BAD_REQUEST, "Email không đúng định dạng"),
	STORE_0600(Constant.STORE_0600, HttpStatus.BAD_REQUEST, "Lỗi xử lý ràng buộc DB"),

	//define error code from spring framework
	//4xx
	STORE_0400(Constant.STORE_0400, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase()),
	STORE_0401(Constant.STORE_0401, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase()),
	STORE_0402(Constant.STORE_0402, HttpStatus.PAYMENT_REQUIRED, HttpStatus.PAYMENT_REQUIRED.getReasonPhrase()),
	STORE_0403(Constant.STORE_0403, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase()),
	STORE_0404(Constant.STORE_0404, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()),
	STORE_0405(Constant.STORE_0405, HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()),
	STORE_0408(Constant.STORE_0408, HttpStatus.REQUEST_TIMEOUT, HttpStatus.REQUEST_TIMEOUT.getReasonPhrase()),
	STORE_0415(Constant.STORE_0415, HttpStatus.UNSUPPORTED_MEDIA_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()),

	//5xx
	STORE_0500(Constant.STORE_0500, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

	private final String errorCode;
	private final HttpStatus status;
	private final String reasonPhrase;

	ErrorCode(String errorCode, HttpStatus status, String reasonPhrase) {
		this.errorCode = errorCode;
		this.status = status;
		this.reasonPhrase = reasonPhrase;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public static class Constant {
		public static final String STORE_0000 = "STORE-0000";
		public static final String STORE_0001 = "STORE-0001";
		public static final String STORE_0003 = "STORE-0003";
		public static final String STORE_0004 = "STORE-0004";
		public static final String STORE_0007 = "STORE-0007";
		public static final String STORE_0008 = "STORE-0008";
		public static final String STORE_0009 = "STORE_0009";
		public static final String STORE_0600 = "STORE-0600";

		//4xx
		public static final String STORE_0400 = "STORE-0400";
		public static final String STORE_0401 = "STORE-0401";
		public static final String STORE_0402 = "STORE-0402";
		public static final String STORE_0403 = "STORE-0403";
		public static final String STORE_0404 = "STORE-0404";
		public static final String STORE_0405 = "STORE-0405";
		public static final String STORE_0408 = "STORE-0408";
		public static final String STORE_0415 = "STORE-0415";

		//5xx
		public static final String STORE_0500 = "STORE-0500";

	}
}
