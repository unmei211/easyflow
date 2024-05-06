package org.star.userservice.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.star.userservice.web.exception.core.ConflictException;
import org.star.userservice.web.exception.feign.FeignErrorDecoder;
import org.star.userservice.web.exception.security.ForbiddenException;
import org.star.userservice.web.exception.security.UnauthorizedException;
import org.star.userservice.web.exception.core.ConflictException;
import org.star.userservice.web.exception.security.ForbiddenException;
import org.star.userservice.web.exception.security.UnauthorizedException;

@Configuration
public class FeignClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        FeignErrorDecoder encoder = new FeignErrorDecoder();
        encoder.mapError(HttpStatus.CONFLICT.value(), ConflictException.class);
        encoder.mapError(HttpStatus.FORBIDDEN.value(), ForbiddenException.class);
        encoder.mapError(HttpStatus.UNAUTHORIZED.value(), UnauthorizedException.class);
        return encoder;
    }
}
