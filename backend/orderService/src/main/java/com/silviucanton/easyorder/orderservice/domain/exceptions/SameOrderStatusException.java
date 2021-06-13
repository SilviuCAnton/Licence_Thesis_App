package com.silviucanton.easyorder.orderservice.domain.exceptions;

public class SameOrderStatusException extends RuntimeException{
    public SameOrderStatusException(String message) {
        super(message);
    }
}
