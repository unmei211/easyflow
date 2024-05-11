package org.star.apigateway.microservice.share.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MicroserviceUserCredentialsResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;

    @Override
    public UserCredentials resolveArgument(
            final @NonNull MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final @NonNull NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) {
        log.info("Try get header");
        String header = Optional.ofNullable(webRequest.getHeader(UserCredentials.USER_CREDENTIALS)).orElseThrow(
                () -> new UnauthorizedException("Invalid header")
        );
        log.info("Resolve userCredentials from headers");
        return UserCredentials.parse(header, mapper).orElseThrow(
                () -> new UnauthorizedException("Error in parse token")
        );
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserCredentials.class);
    }
}
