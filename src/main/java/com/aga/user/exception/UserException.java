package com.aga.user.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter
public class UserException extends RuntimeException {

        private final HttpStatus httpStatus;

        private UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }


    public static final class UserExceptionFactory{

            public static UserException getException(UserExceptionEnum userExceptionEnum, Object...args) {
                return new UserException(String.format(userExceptionEnum.getMessage(), args), userExceptionEnum.getHttpStatus());
            }

    }
}
