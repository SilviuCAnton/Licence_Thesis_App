package com.silviucanton.easyorder.apigatewayservice.webservices;

import com.silviucanton.easyorder.apigatewayservice.domain.exceptions.NoValidAuthorityException;
import com.silviucanton.easyorder.apigatewayservice.domain.exceptions.UserAlreadyExistsException;
import com.silviucanton.easyorder.commons.model.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException badCredentialsException) {
        return getErrorResponse(badCredentialsException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException usernameNotFoundException) {
        return getErrorResponse(usernameNotFoundException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwt(MalformedJwtException malformedJwtException) {
        return getErrorResponse(malformedJwtException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException expiredJwtException) {
        return getErrorResponse(expiredJwtException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArguments(IllegalArgumentException illegalArgumentException) {
        return getErrorResponse(illegalArgumentException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoValidAuthorityException.class)
    public ResponseEntity<ErrorResponse> handleNoValidAuthority(NoValidAuthorityException noValidAuthorityException) {
        return getErrorResponse(noValidAuthorityException, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException userAlreadyExistsException) {
        return getErrorResponse(userAlreadyExistsException, HttpStatus.CONFLICT);
    }
}
