package com.silviucanton.easyorder.orderservice.domain.exceptions;

public class TableNotFoundException
        extends RuntimeException {
    public TableNotFoundException(String message) {
        super(message);
    }
}
