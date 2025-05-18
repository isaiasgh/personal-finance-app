package com.isaias.finance.category_service.domain.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String s) {
        super(s);
    }
}
