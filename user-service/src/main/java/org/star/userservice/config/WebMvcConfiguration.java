package org.star.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.star.apigateway.microservice.share.resolver.MicroserviceUserCredentialsResolver;

import java.util.List;

/**
 * WebConfig. Override argument resolver and interceptors
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final ObjectMapper mapper;

    @Override
    public void addArgumentResolvers(
            final List<HandlerMethodArgumentResolver> resolvers
    ) {
        resolvers.add(new MicroserviceUserCredentialsResolver(mapper));
    }
}
