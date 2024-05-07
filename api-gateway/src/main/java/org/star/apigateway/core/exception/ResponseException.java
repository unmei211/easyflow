package org.star.apigateway.core.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ResponseException {
    private final Integer status;

    private ResponseException(Integer status) {
        this.status = status;
    }

    public static ResponseException build(HttpStatusCode status) {
        return new ResponseException(status.value());
    }
}
