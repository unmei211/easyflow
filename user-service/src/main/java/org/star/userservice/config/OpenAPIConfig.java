package org.star.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "UserService API from Easyflow",
                description = "User service",
                contact = @Contact(
                        name = "Vladislav Starovoytov",
                        email = "unmei.vs@gmail.com"
                )
        )
)
public class OpenAPIConfig {
}
