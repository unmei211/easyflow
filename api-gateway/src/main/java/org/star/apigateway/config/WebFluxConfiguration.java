package org.star.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;
import org.star.apigateway.core.security.resolver.UserCredentialsReactiveResolver;

/**
 * WebConfig. Override argument resolver and interceptors
 */
@Configuration
@AllArgsConstructor
public class WebFluxConfiguration implements WebFluxConfigurer {
    private final ObjectMapper mapper;
    private final ReactiveJwtInterceptor interceptor;

    @Override
    public void configureArgumentResolvers(final ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new UserCredentialsReactiveResolver(mapper, interceptor));
    }
}
