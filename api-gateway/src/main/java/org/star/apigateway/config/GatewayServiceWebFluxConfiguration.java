package org.star.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;
import org.star.apigateway.core.security.resolver.UserCredentialsReactiveResolver;

/**
 * WebConfig. Override argument resolver and interceptors
 */
@Configuration
@AllArgsConstructor
public class GatewayServiceWebFluxConfiguration implements WebFluxConfigurer {
    private final ObjectMapper mapper;
    private final ReactiveJwtInterceptor interceptor;

    @Override
    public void configureArgumentResolvers(final ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new UserCredentialsReactiveResolver(mapper, interceptor));
    }



}
