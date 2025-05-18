package com.isaias.finance.category_service.domain.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String s) {
        super (s);
    }
}