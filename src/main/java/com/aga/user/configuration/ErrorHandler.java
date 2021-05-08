package com.aga.user.configuration;


import com.aga.user.exception.UserException;
import com.aga.user.exception.UserExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<UserExceptionResponse> onUserException(UserException ex) {
        return new ResponseEntity<>(UserExceptionResponse.builder().httpStatus(ex.getHttpStatus()).message(ex.getMessage()).build(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserExceptionResponse> onAnyException(Exception ex) {
        return new ResponseEntity<>(UserExceptionResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
