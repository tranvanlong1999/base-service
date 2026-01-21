package com.example.baseservice.infrastructure.repository;

import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.infrastructure.ITranslationRepo;
import com.example.baseservice.service.entity.Translation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TranslationRepo implements ITranslationRepo {

	private final ITranslationJpa translateRepo;

	@Override
	public Translation save(Translation input) {
		return translateRepo.save(input);
	}

	@Override
	public List<Translation> getHistories() {
		return translateRepo.findAll();
	}

	@Override
	public Optional<Translation> getTranslation(TranslationIn input) {
		return translateRepo.findTranslationByOriginalTextAndSourceAndDestination(input.getText(), input.getSource(), input.getDestination());
	}
}
