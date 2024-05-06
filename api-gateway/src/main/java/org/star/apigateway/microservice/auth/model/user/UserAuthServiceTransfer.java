package org.star.apigateway.microservice.auth.model.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.star.apigateway.core.model.roles.Role;
import org.star.apigateway.core.model.token.RefreshToken;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserAuthServiceTransfer {
    private String id;
    private String login;
    private String email;
    private String password;
    private Boolean enabled;
    private RefreshToken refreshToken;
    private List<Role> roles;
}
