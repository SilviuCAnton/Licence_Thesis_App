package com.silviucanton.easyroder.inventoryservice.domain.exceptions;

public class MenuItemUsedByOrderException extends RuntimeException {
    public MenuItemUsedByOrderException(String message) {
        super(message);
    }
}
