package org.star.easyflow.core.microservice.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MicroserviceAuthorizationFeignInterceptor implements RequestInterceptor {
    private final RequestMappingHandlerMapping handlerMapping;
    @Override
    public void apply(RequestTemplate template) {
        Method method = template.methodMetadata().method();
        MicroserviceAuth annotation =  method.getAnnotation(MicroserviceAuth.class);
        if (annotation == null) {
            return;
        }

        template.header("ServiceAuthorization", "SERVICE_AUTH");
    }
}
