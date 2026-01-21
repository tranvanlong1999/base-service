package com.example.baseservice.infrastructure.repository;

import com.example.baseservice.service.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ITranslationJpa extends JpaRepository<Translation, String> {

	Optional<Translation> findTranslationByOriginalTextAndSourceAndDestination(String originalText, String source, String destination);

}
