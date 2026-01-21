package com.example.baseservice.filter.log;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter
@Getter
public class LogResponse {

	private Map<String, Object> responseBody;

	private Integer status;

}
