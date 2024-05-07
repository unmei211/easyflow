package org.star.apigateway.microservice.share.error.exceptions.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.star.apigateway.microservice.share.error.exceptions.EasyException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends RuntimeException implements EasyException {
    public InternalServerError(String message) {
        super(message);
    }
}
