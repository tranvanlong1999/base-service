package com.example.baseservice.common.model;

import com.google.gson.GsonBuilder;
import com.example.baseservice.common.Common;
import com.example.baseservice.common.ConstantString;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Getter
public class PagingDTO<T> {

    private int page;

    private int maxSize;

    private long totalElement;

    private long totalPages;

    private String sort;

    private String propertiesSort;

    private List<T> data;

    public static Pageable build(PagingDtoIn pagingDtoIn) {
        Sort sort = Common.isNullOrEmpty(pagingDtoIn.getPropertiesSort())
                ? Sort.unsorted()
                : Arrays.stream(pagingDtoIn.getPropertiesSort().split(","))
                .map(property -> new Sort.Order(
                        Sort.Direction.fromString(ObjectUtils.defaultIfNull(pagingDtoIn.getSort(), Sort.Direction.ASC.name())),
                        property))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Sort::by));
        return PageRequest.of(pagingDtoIn.getPage(), pagingDtoIn.getMaxSize(), sort);

    }

    public static <T> PagingDTO<T> get(Page<?> pageResult, PagingDtoIn pagingDtoIn, List<T> result) {
        return PagingDTO.<T>builder()
                .page(pagingDtoIn.getPage())
                .maxSize(pagingDtoIn.getMaxSize())
                .totalElement(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .propertiesSort(pagingDtoIn.getPropertiesSort())
                .sort(pagingDtoIn.getSort())
                .data(result)
                .build();
    }

}
