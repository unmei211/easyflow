package org.star.easyflow.core.microservice.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.star.easyflow.web.exception.security.UnauthorizedException;
import org.star.easyflow.web.model.token.AccessToken;

import java.net.http.HttpHeaders;

@Slf4j
public class AuthorizationResolver implements HandlerMethodArgumentResolver {
    @Override
    public AccessToken resolveArgument(
            final @NonNull MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final @NonNull NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {
        try {
            log.info("Resolve accessToken from headers");
            String headerAccessToken = webRequest.getHeader("Authorization");
            if (headerAccessToken == null || !headerAccessToken.startsWith("Bearer ")) {
                throw new UnauthorizedException("Access token from header is empty");
            }

            String accessToken = headerAccessToken.substring(7);

            return new AccessToken(webRequest.getHeader("Authorization"));
        } catch (Exception e) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AccessToken.class);
    }
}