package com.example.baseservice.service;

import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.dto.out.TranslationOut;

import java.util.List;

public interface ITranslationService {

    TranslationOut translate(TranslationIn input);

    List<TranslationOut> fetchHistories();

}
