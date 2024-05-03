package org.star.apigateway.core.security.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;
import org.star.apigateway.core.security.resolver.adapter.AnnotationGatewayAdapter;
import org.star.apigateway.core.security.user.UserCredentials;
import org.star.apigateway.web.exception.security.UnauthorizedException;
import reactor.core.publisher.Mono;

import javax.management.Attribute;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class UserCredentialsReactiveResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;
    private final ReactiveJwtInterceptor interceptor;

    @Override
    @NonNull
    public Mono<Object> resolveArgument(
            final @NonNull MethodParameter parameter,
            final @NonNull BindingContext bindingContext,
            final @NonNull ServerWebExchange exchange
    ) {
        log.info("resolve arguments");
        HandlerMethod handlerMethod = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        if (handlerMethod == null) {
            return Mono.empty();
        }

        try {
            Method method = handlerMethod.getMethod();
            AuthRoleRequired annotation = method.getAnnotation(AuthRoleRequired.class);
            if (annotation == null) {
                return Mono.empty();
            }

            if (!interceptor.preHandle(exchange, AnnotationGatewayAdapter.getConfig(annotation))) {
                throw new UnauthorizedException("Not enough permission");
            }

            String rawUserCredentials = Objects.requireNonNull(exchange
                            .getRequest()
                            .getHeaders()
                            .get(UserCredentials.USER_CREDENTIALS))
                    .get(0);

            if (rawUserCredentials == null) {
                throw new UnauthorizedException("Invalid");
            }

            UserCredentials userCredentials = UserCredentials.toPresent(rawUserCredentials, mapper);
            if (userCredentials == null) {
                throw new UnauthorizedException("Invalid");
            }
            return Mono.just(userCredentials);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            throw new UnauthorizedException("Error parse token");
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserCredentials.class);
    }
}
