package com.example.baseservice.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class HttpRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest originalRequest;
	private String modifyRequestBody;

	public HttpRequestWrapper(HttpServletRequest originalRequest, String modifyRequestBody) {
		super(originalRequest);
		this.modifyRequestBody = modifyRequestBody;
		this.originalRequest = originalRequest;

	}

	@Override
	@SneakyThrows
	public ServletInputStream getInputStream() {
		return new ServletInputStream() {
			private final InputStream in = new ByteArrayInputStream(modifyRequestBody.getBytes(originalRequest.getCharacterEncoding()));

			@Override
			public int read() throws IOException {
				return in.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}

}