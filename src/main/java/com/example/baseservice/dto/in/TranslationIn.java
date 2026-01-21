package com.example.baseservice.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranslationIn {

	@NotNull(message = "field.not_empty")
	private String text;

	private String source;

	private String destination;

	private String username;

}
