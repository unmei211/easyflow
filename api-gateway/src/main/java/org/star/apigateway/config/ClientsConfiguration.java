package org.star.apigateway.config;

import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import org.star.apigateway.microservice.share.error.handlers.feign.FeignErrorDecoder;

@Configuration
@RequiredArgsConstructor
public class ClientsConfiguration{
    @Bean
    public ErrorsAssociate errorsAssociate() {
        ErrorsAssociate errorsAssociate = new ErrorsAssociate();
        errorsAssociate.mapError(HttpStatus.CONFLICT.value(), ConflictException.class);
        errorsAssociate.mapError(HttpStatus.FORBIDDEN.value(), ForbiddenException.class);
        errorsAssociate.mapError(HttpStatus.UNAUTHORIZED.value(), UnauthorizedException.class);
        errorsAssociate.mapError(HttpStatus.NOT_FOUND.value(), NotFoundException.class);

        return errorsAssociate;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder(errorsAssociate());
    }
}
