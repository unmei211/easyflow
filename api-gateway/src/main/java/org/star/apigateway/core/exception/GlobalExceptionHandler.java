package org.star.apigateway.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ResponseEntity<?> handleForbiddenException(final ForbiddenException ex) {
        System.out.println("check check check");
        log.info("Handle exception {}", ex);
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(ResponseException.build(status));
    }

}
