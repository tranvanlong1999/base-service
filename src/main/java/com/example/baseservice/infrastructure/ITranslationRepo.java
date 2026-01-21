package com.example.baseservice.infrastructure;


import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.service.entity.Translation;

import java.util.List;
import java.util.Optional;

public interface ITranslationRepo {

    Optional<Translation> getTranslation(TranslationIn input);

    List<Translation> getHistories();

    Translation save(Translation input);

}
