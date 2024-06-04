package org.star.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.star.apigateway.core.entity.password.Password;
import org.star.apigateway.core.entity.token.RefreshToken;
import org.star.apigateway.core.entity.user.UserAuth;
import org.star.apigateway.core.factory.EntityFactory;
import org.star.apigateway.core.factory.PasswordFactory;
import org.star.apigateway.core.factory.RefreshTokenFactory;
import org.star.apigateway.core.factory.UserAuthFactory;

@Configuration
public class EntityFactoryConfig {
    @Bean(name = "userFactory")
    public EntityFactory<UserAuth.UserAuthBuilder> userAuthFactory() {
        return new UserAuthFactory();
    }

    @Bean(name = "passwordFactory")
    public EntityFactory<Password.PasswordBuilder> passwordFactory() {
        return new PasswordFactory();
    }

    @Bean(name = "refreshTokenFactory")
    public EntityFactory<RefreshToken.RefreshTokenBuilder> refreshTokenFactory() {
        return new RefreshTokenFactory();
    }

}
