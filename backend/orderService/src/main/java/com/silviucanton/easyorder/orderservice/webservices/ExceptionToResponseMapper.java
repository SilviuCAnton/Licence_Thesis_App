package com.silviucanton.easyorder.orderservice.webservices;


import com.silviucanton.easyorder.commons.model.ErrorResponse;
import com.silviucanton.easyorder.orderservice.domain.exceptions.MenuItemNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.OrderNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.SameOrderStatusException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TableNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TempOrderConflictException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TempOrderNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.WaiterNotFoundException;
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

    @ExceptionHandler(value = TempOrderConflictException.class)
    public ResponseEntity<ErrorResponse> handleTempOrderConflict(TempOrderConflictException tempOrderConflictException) {
        return getErrorResponse(tempOrderConflictException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TableNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTableNotFound(TableNotFoundException tableNotFoundException) {
        return getErrorResponse(tableNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MenuItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMenuItemNotFound(MenuItemNotFoundException menuItemNotFoundException) {
        return getErrorResponse(menuItemNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(IllegalArgumentException illegalArgumentException) {
        return getErrorResponse(illegalArgumentException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = WaiterNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWaiterNotFound(WaiterNotFoundException waiterNotFoundException) {
        return getErrorResponse(waiterNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SameOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleSameOrderStatus(SameOrderStatusException sameOrderStatusException) {
        return getErrorResponse(sameOrderStatusException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException orderNotFoundException) {
        return getErrorResponse(orderNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TempOrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTempOrderNotFound(TempOrderNotFoundException tempOrderNotFoundException) {
        return getErrorResponse(tempOrderNotFoundException, HttpStatus.NOT_FOUND);
    }
}
