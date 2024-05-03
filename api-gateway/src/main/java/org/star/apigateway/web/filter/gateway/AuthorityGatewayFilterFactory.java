package org.star.apigateway.web.filter.gateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.star.apigateway.core.model.roles.RolesEnum;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorityGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorityGatewayFilterFactory.Config> {
    private final ReactiveJwtInterceptor interceptor;

    public AuthorityGatewayFilterFactory(
            final @Qualifier("ReactiveInterceptor") ReactiveJwtInterceptor interceptor
    ) {
        super(Config.class);
        this.interceptor = interceptor;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            System.out.println("filter");
            if (interceptor.preHandle(exchange, config)) {
                return chain.filter(exchange);
            }
            return exchange.getResponse().setComplete();
        });
    }

    @Getter
    @Setter
    public static class Config {
        private List<RolesEnum> access = new ArrayList<>();
        private boolean anyRole = false;
        private boolean publicEndpoint = false;

        public boolean requiredProcess() {
            return !access.isEmpty() || anyRole;
        }
    }

}
