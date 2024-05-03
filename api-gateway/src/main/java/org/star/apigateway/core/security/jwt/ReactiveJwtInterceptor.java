package org.star.apigateway.core.security.jwt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.star.apigateway.core.model.roles.RolesEnum;
import org.star.apigateway.core.security.gateway.GatewayInterceptor;
import org.star.apigateway.core.security.user.UserCredentials;
import org.star.apigateway.core.service.auth.AuthService;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.web.exception.security.UnauthorizedException;
import org.star.apigateway.web.filter.gateway.AuthorityGatewayFilterFactory;

import java.util.List;


@Component
@Qualifier("ReactiveInterceptor")
@AllArgsConstructor
@Slf4j
public class ReactiveJwtInterceptor implements GatewayInterceptor {
    private final JwtService jwtService;
    private final AuthService authService;

    @Override
    public boolean preHandle(
            final ServerWebExchange exchange,
            final Object config
    ) {
        log.info("pre handle");
        if (config.getClass().equals(AuthorityGatewayFilterFactory.Config.class)) {
            AuthorityGatewayFilterFactory.Config configuration = (AuthorityGatewayFilterFactory.Config) config;
            return gatewayAuthorization(exchange, configuration);
        }
        return true;
    }

    private boolean gatewayAuthorization(
            final ServerWebExchange exchange,
            final AuthorityGatewayFilterFactory.Config config
    ) {
        log.info("Try gatewayAuthorization");
        log.info(config.getAccess().toString());
        try {
            if (config.requiredProcess()) {
                log.info("RequiredProcess");
                UserCredentials userCredentials = getUserCredentials(exchange);
                if (userCredentials == null) {
                    throw new UnauthorizedException("Not found authorization header but required");
                }

                if (!authService.findById(userCredentials.getUserId()).getEnabled()) {
                    throw new UnauthorizedException("User blocked");
                }

                if (!config.getAccess().isEmpty()) {
                    for (RolesEnum role : config.getAccess()) {
                        if (!userCredentials.getRoles().contains(role.toString())) {
                            throw new UnauthorizedException("Unauthorized with access type");
                        }
                    }
                    return true;
                }

                if (config.isAnyRole()) {
                    for (RolesEnum role : config.getAccess()) {
                        if (userCredentials.getRoles().contains(role.toString())) {
                            return true;
                        }
                    }
                    throw new UnauthorizedException("Not authorize with anyRole");
                }
            }
            return true;
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }

    public UserCredentials getUserCredentials(
            final ServerWebExchange exchange
    ) {
        try {
            List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null ||
                    authHeaders.get(0) == null ||
                    !authHeaders.get(0).startsWith("Bearer ")
            ) {
                return null;
            }
            final String header = authHeaders.get(0);
            final int bearerIndex = 7;
            String token = header.substring(bearerIndex);
            UserCredentials credentials = jwtService.parseToken(token);
            log.info("Found credentials in Authorization header: {}", credentials.getUserId());
            System.out.println(credentials.toString());
            exchange.getRequest().mutate().headers(httpHeaders -> {
                httpHeaders.add(UserCredentials.USER_CREDENTIALS, credentials.toString());
            });
            return credentials;
        } catch (Exception e) {
            return null;
        }
    }
}
