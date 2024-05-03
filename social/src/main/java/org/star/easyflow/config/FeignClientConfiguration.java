package org.star.easyflow.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.star.easyflow.core.microservice.client.interceptor.MicroserviceAuthorizationFeignInterceptor;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfiguration {
    private final RequestMappingHandlerMapping handlerMapping;

    @Bean
    public MicroserviceAuthorizationFeignInterceptor microserviceRightsFeignInterceptor() {
        return new MicroserviceAuthorizationFeignInterceptor(handlerMapping);
    }
}
