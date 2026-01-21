/**
 * ****************************************************************************
 * Copyright (c) 2017 ANHTCN.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * *****************************************************************************
 */
package com.example.baseservice.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Common {

	private static final Map<Class<?>, Class<?>> primitiveClassMap = new HashMap<>();

	public static final Snowflake snowflake = new Snowflake();

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static String GenerateUUID() {
		return UUID.randomUUID().toString();
	}

	public static java.sql.Timestamp getCurrentTime() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	static {
		primitiveClassMap.put(Short.class, short.class);
		primitiveClassMap.put(Integer.class, int.class);
		primitiveClassMap.put(Long.class, long.class);
		primitiveClassMap.put(Float.class, float.class);
		primitiveClassMap.put(Double.class, double.class);
		primitiveClassMap.put(Boolean.class, boolean.class);
		primitiveClassMap.put(Byte.class, byte.class);
		primitiveClassMap.put(Character.class, char.class);
		primitiveClassMap.put(Void.class, void.class);
	}

	public static Class<?> getPrimitiveClassObject(Class<?> clazz) {
		return primitiveClassMap.get(clazz);
	}

}
