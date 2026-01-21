package com.example.baseservice.common.context;

public class DataContextHolder {
	private static final ThreadLocal<DataContext> contextHolder = new ThreadLocal<>();

	static void setDataContext(DataContext dataContext) {
		contextHolder.set(dataContext);
	}

	static DataContext getDataContext() {
		return contextHolder.get();
	}

	static void clear() {
		contextHolder.remove();
	}
}
