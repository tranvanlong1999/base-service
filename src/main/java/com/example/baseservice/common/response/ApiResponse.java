package com.example.baseservice.common.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.example.baseservice.common.Common;
import com.example.baseservice.common.json.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public class ApiResponse {

	private final JsonElement json;

	private ApiResponse() {
		throw new AssertionError("Can not call constructor default");
	}

	/**
	 * Hàm parser đối tượng không phải generic
	 *
	 * @param clazz kiểu đối tượng cần parser
	 * @param <T>   kiểu object cần trả về
	 * @return trả về hàm khởi tạo của đối tượng nếu node null ngược lại trả về đối tượng parser
	 */
	public <T> T parser(Class<T> clazz) {
		return json.isJsonNull() ? parserJsonConstructor(clazz) : JsonUtils.fromJson(json, clazz);
	}

	/**
	 * Hàm parser đối tượng generic
	 *
	 * @param type kiểu cần parser là dạng generic
	 * @param <T>  kiểu object cần trả về
	 * @return null nếu node null
	 */
	public <T> T parser(Type type) {
		return JsonUtils.fromJson(json, type);
	}

	public ApiResponse getObject(String key) {
		if (json == null || json.isJsonNull()) return new ApiResponse(JsonNull.INSTANCE);
		JsonElement node = json.getAsJsonObject().get(key);
		return node == null ? new ApiResponse(JsonNull.INSTANCE) : new ApiResponse(node);
	}

	private <T> T parserJsonConstructor(Class<T> clazz) {
		try {
			if (Number.class.isAssignableFrom(clazz))
				return clazz.getDeclaredConstructor(Common.getPrimitiveClassObject(clazz)).newInstance(0);
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
				 InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
