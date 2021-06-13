package com.silviucanton.easyorder.orderservice.domain.exceptions;

public class TempOrderNotFoundException extends RuntimeException {

    public TempOrderNotFoundException(String message) {
        super(message);
    }
}
