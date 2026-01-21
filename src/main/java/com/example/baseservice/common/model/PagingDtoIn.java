package com.example.baseservice.common.model;

import com.example.baseservice.common.exception.model.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


@Data
public class PagingDtoIn {

	private int page = 1;

	@NotNull(message = ErrorCode.Constant.STORE_0001)
	@Range(min = 1, message = ErrorCode.Constant.STORE_0004)
	private Integer maxSize = 100;

	private String keySearch;

	private String sort;

	private String propertiesSort;
}
