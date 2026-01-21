package com.example.baseservice.common.context;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class DataContext {
	public String username;
	public String clientId;
	public List<String> authorities;
	public String uuidAccountLogin;

	@Builder.Default
	private Map<String, Object> otherData = new HashMap<>();
}
