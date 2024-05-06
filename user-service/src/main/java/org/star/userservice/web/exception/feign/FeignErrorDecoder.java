package org.star.userservice.web.exception.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.star.userservice.web.exception.core.ConflictException;
import org.star.userservice.web.exception.security.ForbiddenException;
import org.star.userservice.web.exception.security.UnauthorizedException;

import java.util.HashMap;

public class FeignErrorDecoder implements ErrorDecoder {
    private final HashMap<Integer, Class<? extends RuntimeException>> errorsMap;

    public FeignErrorDecoder() {
        errorsMap = new HashMap<>();
    }

    public void mapError(Integer status, Class<? extends RuntimeException> exception) {
        errorsMap.put(status, exception);
    }

    @Override
    public Exception decode(String s, Response response) {
        if (errorsMap.containsKey(response.status())) {
            Class<? extends RuntimeException> exceptionClass = errorsMap.get(response.status());

            try {
                return exceptionClass.getDeclaredConstructor(String.class).newInstance("");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
