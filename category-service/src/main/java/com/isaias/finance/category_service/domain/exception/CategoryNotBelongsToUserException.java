package com.isaias.finance.category_service.domain.exception;

public class CategoryNotBelongsToUserException extends RuntimeException {
    public CategoryNotBelongsToUserException(String s) {
        super (s);
    }
}
