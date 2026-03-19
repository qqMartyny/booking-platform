package com.ilyanin.booking_platform.shared;

import java.util.List;

public record PageResult<T>(
    List<T> content,
    int page,
    int pageSize,
    long totalElements
) {

    public PageResult{
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements cannot be negative: " + totalElements);
        }
        if (page < 0) {
            throw new IllegalArgumentException("page cannot be negative: " + page);
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be positive: " + pageSize);
        }
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
    }

    public int totalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}
