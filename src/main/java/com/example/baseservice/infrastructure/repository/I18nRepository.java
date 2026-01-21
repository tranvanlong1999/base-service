package com.example.baseservice.infrastructure.repository;


import com.example.baseservice.service.entity.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface I18nRepository extends JpaRepository<ErrorMessage, Integer> {

    ErrorMessage findByKeyAndLocale(String key, String locale);

}
