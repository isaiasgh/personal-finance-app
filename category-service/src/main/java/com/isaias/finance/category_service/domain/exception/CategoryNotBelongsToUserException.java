package com.isaias.finance.category_service.domain.exception;

public class CategoryNotBelongsToUserException extends RuntimeException {
    public CategoryNotBelongsToUserException(Long id, String username) {
        super ("Category " + id + " does not belong to user: " + username);
    }
}