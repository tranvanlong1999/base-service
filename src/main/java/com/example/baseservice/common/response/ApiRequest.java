package com.example.baseservice.common.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ApiRequest {

	private String host;

	private String path;

	@Setter
	private HttpEntity<?> entity;

	private Duration connectionTimeout;

	private Duration readTimeout;

	@Builder.Default
	private HttpMethod method = HttpMethod.POST;

	@Builder.Default
	private Map<String, Object> parameters = new HashMap<>();

	private HttpServletRequest request;

}
