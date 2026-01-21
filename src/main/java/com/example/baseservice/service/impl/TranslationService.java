package com.example.baseservice.service.impl;

import com.example.baseservice.context.DataContextHelper;
import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.dto.out.TranslationOut;
import com.example.baseservice.infrastructure.IExtTranslationRepo;
import com.example.baseservice.infrastructure.ITranslationRepo;
import com.example.baseservice.service.ITranslationService;
import com.example.baseservice.service.entity.Translation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TranslationService implements ITranslationService {

    private final IExtTranslationRepo extTranslationRepo;
    private final ITranslationRepo translationRepo;

    public TranslationService(IExtTranslationRepo extTranslationRepo, ITranslationRepo translationRepo) {
        this.extTranslationRepo = extTranslationRepo;
        this.translationRepo = translationRepo;
    }

    @Override
    public List<TranslationOut> fetchHistories() {
        return translationRepo.getHistories()
                .stream()
                .map(translation -> TranslationOut.builder()
                        .originalText(translation.getOriginalText())
                        .source(translation.getSource())
                        .destination(translation.getDestination())
                        .resultText(translation.getResultText())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public TranslationOut translate(TranslationIn input) {
        System.out.println("Dòng 44 " + DataContextHelper.getUsername());
        Optional<Translation> optional = translationRepo.getTranslation(input);
        if (optional.isEmpty()) {
            TranslationOut translateOut = extTranslationRepo.translate(input);
            Translation translation = new Translation();
            translation.setId(UUID.randomUUID().toString());
            translation.setOriginalText(input.getText());
            translation.setSource(input.getSource());
            translation.setDestination(input.getDestination());
            translation.setResultText(translateOut.getResultText());
            optional = Optional.of(translationRepo.save(translation));
        }
        return TranslationOut.builder()
                .originalText(optional.get().getOriginalText())
                .source(input.getSource())
                .destination(input.getDestination())
                .resultText(optional.get().getResultText())
                .build();
    }
}
