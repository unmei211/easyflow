package org.star.apigateway.core.security.gateway;

import org.springframework.web.server.ServerWebExchange;

public interface GatewayInterceptor {
    boolean preHandle(ServerWebExchange exchange, Object config);
}
