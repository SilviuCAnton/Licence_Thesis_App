package com.silviucanton.easyorder.logisticsservice.domain.exceptions;

public class WaiterNotFoundException extends RuntimeException {
    public WaiterNotFoundException(String message) {
        super(message);
    }
}
