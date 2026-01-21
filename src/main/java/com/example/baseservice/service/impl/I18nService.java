package com.example.baseservice.service.impl;

import com.example.baseservice.infrastructure.repository.I18nRepository;
import com.example.baseservice.service.entity.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class I18nService {

	private final I18nRepository i18nRepository;

	private static final String DEFAULT_LOCALE_CODE = "en";

	public MessageFormat resolveMessage(String key, Locale locale) {
		ErrorMessage message = i18nRepository.findByKeyAndLocale(key, locale.getLanguage());
		if (message == null) {
			message = i18nRepository.findByKeyAndLocale(key, DEFAULT_LOCALE_CODE);
		}
		return new MessageFormat(message.getContent(), locale);
	}

}
