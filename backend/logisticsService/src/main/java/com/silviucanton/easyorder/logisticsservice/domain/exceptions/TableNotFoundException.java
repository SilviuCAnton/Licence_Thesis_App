package com.silviucanton.easyorder.logisticsservice.domain.exceptions;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String message) {
        super(message);
    }
}
