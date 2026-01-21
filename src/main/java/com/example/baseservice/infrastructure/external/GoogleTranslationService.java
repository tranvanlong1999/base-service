package com.example.baseservice.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.dto.out.TranslationOut;
import com.example.baseservice.infrastructure.IExtTranslationRepo;

@Service
@RequiredArgsConstructor
public class GoogleTranslationService implements IExtTranslationRepo {

	private final String url = "https://translate.googleapis.com/translate_a/single";

	private final RestClient restClient = RestClient.builder().build();

	@Override
	public TranslationOut translate(TranslationIn input) {
		UriComponents url = UriComponentsBuilder.fromHttpUrl(this.url)
				.queryParam("client", "gtx")
				.queryParam("sl", input.getSource())
				.queryParam("tl", input.getDestination())
				.queryParam("hl", input.getDestination())
				.queryParam("dt", "t")
				.queryParam("dt", "bd")
				.queryParam("dj", "1")
				.queryParam("source", "icon")
				.queryParam("tk", "464105.464105")
				.queryParam("q", input.getText()).build();
		JsonNode jsonNode = restClient.get().uri(url.toUri())
				.retrieve()
				.body(JsonNode.class);

		if (jsonNode == null || jsonNode.get("sentences") == null) {
			throw new RuntimeException("Cannot translate");
		}
		return TranslationOut.builder()
				.destination(input.getDestination())
				.source(input.getSource())
				.originalText(input.getText())
				.resultText(jsonNode.get("sentences").get(0).get("trans").asText())
				.build();
	}
}
