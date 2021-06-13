package com.silviucanton.easyorder.logisticsservice.webservices;


import com.silviucanton.easyorder.commons.model.ErrorResponse;
import com.silviucanton.easyorder.logisticsservice.domain.exceptions.TableNotFoundException;
import com.silviucanton.easyorder.logisticsservice.domain.exceptions.WaiterNotFoundException;
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

    @ExceptionHandler(value = TableNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTableNotFound(TableNotFoundException tableNotFoundException) {
        return getErrorResponse(tableNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(IllegalArgumentException illegalArgumentException) {
        return getErrorResponse(illegalArgumentException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = WaiterNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWaiterNotFound(WaiterNotFoundException waiterNotFoundException) {
        return getErrorResponse(waiterNotFoundException, HttpStatus.NOT_FOUND);
    }

}
