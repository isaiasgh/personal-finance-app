package com.isaias.finance.category_service.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super ("Category: " + id + " was not found");
    }

    public CategoryNotFoundException(String categoryName) {
        super ("Category " + categoryName + " was not found");
    }
}
