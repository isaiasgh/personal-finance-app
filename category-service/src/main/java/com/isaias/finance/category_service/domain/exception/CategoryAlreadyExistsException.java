package com.isaias.finance.category_service.domain.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryName, String username) {
        super("Category with name: " + categoryName + " for user: " + username + " already exists.");
    }

    public CategoryAlreadyExistsException(String categoryName) {
        super("Category with name: " + categoryName + " is a base category.");
    }
}
