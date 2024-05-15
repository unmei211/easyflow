package org.star.apigateway.microservice.share.error.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.star.apigateway.microservice.share.error.exceptions.core.InternalServerError;
import org.star.apigateway.microservice.share.error.exceptions.core.ServiceUnavailable;

import java.util.HashMap;

public class ErrorsAssociate {
    private final HashMap<Integer, Class<? extends RuntimeException>> errorsMap;

    public ErrorsAssociate() {
        this.errorsMap = new HashMap<>();
    }

    private void letThrow(Integer status) {
        if (!errorsMap.containsKey(status)) {
            throw new InternalServerError("Can't found mapping of error");
        }

        Class<? extends RuntimeException> exceptionClass = errorsMap.get(status);

        try {
            throw exceptionClass.getDeclaredConstructor(String.class).newInstance("");
        } catch (Exception e) {
            throw new InternalServerError("Service unavailable");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends RuntimeException> T letInit(Integer status) {
        if (!errorsMap.containsKey(status)) {
            return (T) new InternalServerError("Can't map");
        }

        try {
            T t = (T) errorsMap.get(status).getDeclaredConstructor(String.class).newInstance("");
            return (T) errorsMap.get(status).getDeclaredConstructor(String.class).newInstance("");
        } catch (Exception e) {
            throw new InternalServerError("Service unavailable");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends RuntimeException, R extends HttpStatusCode> T letInit(String message, R status) {
        if (!errorsMap.containsKey(status.value())) {
            return (T) new InternalServerError("Can't map");
        }

        try {
            T t = (T) errorsMap.get(status.value()).getDeclaredConstructor(String.class).newInstance(message);
            return (T) errorsMap.get(status.value()).getDeclaredConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new InternalServerError("Service unavailable");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends RuntimeException> T letInit(String message, Integer status) {
        if (!errorsMap.containsKey(status)) {
            return (T) new InternalServerError("Can't map");
        }

        try {
            T t = (T) errorsMap.get(status).getDeclaredConstructor(String.class).newInstance(message);
            return (T) errorsMap.get(status).getDeclaredConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new InternalServerError("Service unavailable");
        }
    }


    public void ifStatus(Integer status) {
        letThrow(status);
    }

    public <T extends HttpStatusCode> void ifStatus(T httpStatus) {
        letThrow(httpStatus.value());
    }

    public boolean containsKey(HttpStatus httpStatus) {
        return this.errorsMap.containsKey(httpStatus.value());
    }

    public boolean containsKey(Integer status) {
        return this.errorsMap.containsKey(status);
    }

    public Class<? extends RuntimeException> get(Integer status) {
        return errorsMap.get(status);
    }

    public Class<? extends RuntimeException> get(HttpStatus status) {
        return errorsMap.get(status.value());
    }

    public void mapError(Integer status, Class<? extends RuntimeException> exception) {
        errorsMap.put(status, exception);
    }

    public void mapError(HttpStatus status, Class<? extends RuntimeException> exception) {
        errorsMap.put(status.value(), exception);
    }


    public <T extends RuntimeException> T getInit(Integer status) {
        return letInit(status);
    }

    public <T extends RuntimeException, U extends HttpStatusCode> T getInit(U status) {
        return letInit(status.value());
    }

    public <T extends RuntimeException> T getInit(String message, Integer status) {
        return letInit(message, status);
    }

    public <T extends RuntimeException, U extends HttpStatusCode> T getInit(String message, U status) {
        return letInit(message, status.value());
    }

}
