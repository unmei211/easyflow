package org.star.easyflow.core.microservice.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.star.easyflow.web.exception.security.UnauthorizedException;
import org.star.easyflow.web.model.user.UserCredentials;

@Slf4j
@AllArgsConstructor
public class UserCredentialsResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper mapper;

    @Override
    public UserCredentials resolveArgument(
            final @NonNull MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final @NonNull NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {
        try {
            log.info("Resolve userCredentials from headers");
            return UserCredentials.toPresent(webRequest.getHeader(UserCredentials.USER_CREDENTIALS), mapper);
        } catch (Exception e) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserCredentials.class);
    }
}
