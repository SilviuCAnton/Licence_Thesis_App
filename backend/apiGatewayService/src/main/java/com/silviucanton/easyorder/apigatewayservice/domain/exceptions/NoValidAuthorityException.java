package com.silviucanton.easyorder.apigatewayservice.domain.exceptions;

public class NoValidAuthorityException extends RuntimeException {
    public NoValidAuthorityException(String message) {
        super(message);
    }
}
