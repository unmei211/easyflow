package org.star.apigateway.web.exception.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityExistException extends RuntimeException {
    public EntityExistException(String message) {
        super(message);
    }
}
