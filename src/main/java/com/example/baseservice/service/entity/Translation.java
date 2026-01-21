package com.example.baseservice.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "translation")
public class Translation {

	@Id
	private String id;

	@Column(name = "original_text")
	private String originalText;

	private String source;

	private String destination;

	@Column(name = "result_text")
	private String resultText;

}
