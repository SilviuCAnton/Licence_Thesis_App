package com.silviucanton.easyroder.inventoryservice.webservices;


import com.silviucanton.easyorder.commons.model.ErrorResponse;
import com.silviucanton.easyroder.inventoryservice.domain.exceptions.MenuItemNotFoundException;
import com.silviucanton.easyroder.inventoryservice.domain.exceptions.MenuItemUsedByOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionToResponseMapper extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorResponse> getErrorResponse(RuntimeException exception, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(),
                                                        exception.getMessage(),
                                                        System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(value = MenuItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMenuItemNotFound(MenuItemNotFoundException menuItemNotFoundException) {
        return getErrorResponse(menuItemNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(IllegalArgumentException illegalArgumentException) {
        return getErrorResponse(illegalArgumentException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MenuItemUsedByOrderException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(MenuItemUsedByOrderException menuItemUsedByOrderException) {
        return getErrorResponse(menuItemUsedByOrderException, HttpStatus.CONFLICT);
    }
}
