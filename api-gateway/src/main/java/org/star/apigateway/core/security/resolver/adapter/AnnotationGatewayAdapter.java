package org.star.apigateway.core.security.resolver.adapter;

import org.star.apigateway.core.gateway.AuthorityGatewayFilterFactory;
import org.star.apigateway.core.model.roles.RolesEnum;
import org.star.apigateway.core.security.resolver.AuthRoleRequired;

import java.util.Arrays;
import java.util.List;

public class AnnotationGatewayAdapter {
    public static AuthorityGatewayFilterFactory.Config getConfig(final AuthRoleRequired authRoleRequired) {
        AuthorityGatewayFilterFactory.Config config = new AuthorityGatewayFilterFactory.Config();

        if (authRoleRequired.anyRole()) {
            config.setAnyRole(true);
        } else if (!authRoleRequired.value().isEmpty()) {
            config.setAccess(List.of(RolesEnum.valueOf(authRoleRequired.value())));
        } else if (authRoleRequired.required().length > 0) {
            config.setAccess(Arrays.stream(authRoleRequired.required())
                    .map(RolesEnum::valueOf).toList());
        } else {
            config.setPublicEndpoint(true);
        }

        return config;
    }
}
