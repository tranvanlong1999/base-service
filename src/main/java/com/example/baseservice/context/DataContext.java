package com.example.baseservice.context;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class DataContext {

	private String username;

	private String userId;

	private String accessToken;

	private List<String> authorities;

	@Builder.Default
	private Map<Enum<?>, Object> otherData = new HashMap<>();

}
