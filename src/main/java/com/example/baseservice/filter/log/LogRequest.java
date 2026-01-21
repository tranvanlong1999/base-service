package com.example.baseservice.filter.log;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
public class LogRequest {

	private String path;

	private String method;

	private Request request;

	@Builder
	@Setter
	@Getter
	public static class Request {

		private Map<String, Object> headers;

		private Map<String, Object> params;

		private Map<String, Object> requestBody;

	}

}
