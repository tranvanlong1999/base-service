package com.example.baseservice.dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranslationOut {

	private String originalText;

	private String source;

	private String destination;

	private String resultText;

}
