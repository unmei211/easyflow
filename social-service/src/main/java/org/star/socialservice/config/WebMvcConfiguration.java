package org.star.socialservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.star.socialservice.core.microservice.resolver.AuthorizationResolver;
import org.star.socialservice.core.microservice.resolver.UserCredentialsResolver;

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
        resolvers.add(new AuthorizationResolver());
        resolvers.add(new UserCredentialsResolver(mapper));
    }
}
