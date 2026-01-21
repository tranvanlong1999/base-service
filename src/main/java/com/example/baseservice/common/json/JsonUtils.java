package com.example.baseservice.common.json;

import com.google.gson.*;
import com.example.baseservice.common.ConstantString;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;

@UtilityClass
public class JsonUtils {

	private static final Gson Gson = new GsonBuilder().setDateFormat(ConstantString.DDMMYYYY_HHMMSS).serializeNulls().create();

	public static String toJson(Object obj) {
		return Gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return Gson.fromJson(json, clazz);
	}

	public static <T> T fromJson(JsonElement json, Type type) {
		return Gson.fromJson(json, type);
	}

	public static <T> T fromJson(JsonElement json, Class<T> clazz) {
		return Gson.fromJson(json, clazz);
	}

	public static boolean isValid(String json) {
		try {
			JsonParser.parseString(json);
		} catch (JsonSyntaxException e) {
			return false;
		}
		return true;
	}

}
