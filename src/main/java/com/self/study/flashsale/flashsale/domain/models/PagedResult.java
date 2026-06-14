package com.self.study.flashsale.flashsale.domain.models;

import java.util.List;

public class PagedResult<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int pageNumber;
    private final int pageSize;

    public PagedResult(List<T> content, long totalElements, int totalPages, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
