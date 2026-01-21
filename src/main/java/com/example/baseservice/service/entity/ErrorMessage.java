package com.example.baseservice.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "error_message")
public class ErrorMessage {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "LOCALE")
	private String locale;

	@Column(name = "KEY")
	private String key;

	@Column(name = "CONTENT")
	private String content;

}
