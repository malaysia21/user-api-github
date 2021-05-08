package com.aga.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionEnum {

    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"User with login: %s not found."),
    USER_OTHER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: %s."),
    USER_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "Validation error: %s.");

    private final HttpStatus httpStatus;
    private final String message;
}
