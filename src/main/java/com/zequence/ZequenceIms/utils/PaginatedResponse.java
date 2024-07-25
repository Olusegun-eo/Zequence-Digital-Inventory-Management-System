package com.zequence.ZequenceIms.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    @JsonProperty("content")
    private List<T> content;
    @JsonProperty("pageNumber")
    private int pageNumber;
    @JsonProperty("pageSize")
    private int pageSize;
    @JsonProperty("totalElements")
    private long totalElements;
    @JsonProperty("totalPages")
    private int totalPages;
}