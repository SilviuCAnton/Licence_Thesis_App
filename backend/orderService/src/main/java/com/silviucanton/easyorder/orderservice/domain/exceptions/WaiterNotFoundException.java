package com.silviucanton.easyorder.orderservice.domain.exceptions;

public class WaiterNotFoundException
        extends RuntimeException {
    public WaiterNotFoundException(String message) {
        super(message);
    }
}
