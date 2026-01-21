package com.example.baseservice.infrastructure;


import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.dto.out.TranslationOut;

public interface IExtTranslationRepo {

    TranslationOut translate(TranslationIn input);

}
