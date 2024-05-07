package org.star.apigateway.microservice.share.error.handlers.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;

import java.util.HashMap;

@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorsAssociate associate;

    @Override
    public Exception decode(String s, Response response) {
        if (associate.containsKey(response.status())) {
            Class<? extends RuntimeException> exceptionClass = associate.get(response.status());

            try {
                return exceptionClass.getDeclaredConstructor(String.class).newInstance("");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
