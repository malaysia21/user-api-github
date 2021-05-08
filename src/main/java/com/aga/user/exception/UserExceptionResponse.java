package com.aga.user.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class UserExceptionResponse {

    private final HttpStatus httpStatus;
    private final String message;
}
