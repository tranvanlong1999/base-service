package com.example.baseservice.common;

public class ConstantString {

	public static final long TIMEOUT_NONBLOCK = 300000L;
	public static final int ZERO = 0;
	public static final int MAX_SIZE = 100;
	public static final String EMPTY_STR = "";
	public static final String GZIP = "gzip";

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String DDMMYYYY_HHMMSS = "dd/MM/yyyy HH:mm:ss";
	public static final String DDMMYYYY = "dd/MM/yyyy";

	public static final class ProcessingStatus {
		public static final int SUCCESS = 1;
		public static final int PROCESSING = 0;
	}

	public static class StatusDB {
		public static final String SUCCESS = "success";
		public static final String EXIST = "EXIST";
		public static final String NOT_FOUND = "NOT_FOUND";
	}

}
