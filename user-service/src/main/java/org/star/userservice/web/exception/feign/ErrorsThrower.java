package org.star.userservice.web.exception.feign;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.star.apigateway.web.exception.security.ForbiddenException;
import org.star.userservice.web.exception.core.ConflictException;
import org.star.userservice.web.exception.security.UnauthorizedException;

import java.util.HashMap;

public class ErrorsThrower {
    private static final HashMap<Integer, Class<? extends RuntimeException>> errorsMap;

    static {
        errorsMap = new HashMap<>();
        errorsMap.put(HttpStatus.CONFLICT.value(), ConflictException.class);
        errorsMap.put(HttpStatus.FORBIDDEN.value(), ForbiddenException.class);
        errorsMap.put(HttpStatus.UNAUTHORIZED.value(), UnauthorizedException.class);
    }

    public static void ifHas(final FeignException exception, String message) {
        if (errorsMap.containsKey(exception.status())) {
            Class<? extends RuntimeException> exceptionClass = errorsMap.get(exception.status());
            try {
                throw exceptionClass.getDeclaredConstructor(String.class).newInstance(message);
            } catch (Exception e) {
                throw exception;
            }
        }
        throw exception;
    }
}
