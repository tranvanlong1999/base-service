package com.example.baseservice.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.bouncycastle.util.io.TeeOutputStream;
import org.springframework.lang.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public HttpResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		final ServletOutputStream servletOutputStream = HttpResponseWrapper.super.getOutputStream();
		return new ServletOutputStream() {
			private final TeeOutputStream tee = new TeeOutputStream(servletOutputStream, bos);

			@Override
			public void write(@NonNull byte[] b) throws IOException {
				tee.write(b);
			}

			@Override
			public void write(@NonNull byte[] b, int off, int len) throws IOException {
				tee.write(b, off, len);
			}

			@Override
			public void flush() throws IOException {
				tee.flush();
			}

			@Override
			public void write(int b) throws IOException {
				tee.write(b);
			}

			@Override
			public boolean isReady() {
				return servletOutputStream.isReady();
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {
				servletOutputStream.setWriteListener(writeListener);
			}


			@Override
			public void close() throws IOException {
				super.close();
			}
		};
	}

	public String getResponseBody() {
		byte[] toLog = toByteArray();
		return new String(toLog);
	}

	public byte[] toByteArray() {
		byte[] ret = bos.toByteArray();
		bos.reset();
		return ret;
	}
}