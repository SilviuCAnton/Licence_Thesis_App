package com.silviucanton.easyorder.orderservice.domain.exceptions;

public class TempOrderConflictException extends RuntimeException {
    public TempOrderConflictException(String message) {
        super(message);
    }
}
