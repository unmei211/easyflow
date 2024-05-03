package org.star.apigateway.web.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.apigateway.core.model.user.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Whoami {
    private String id;
    private String login;
    private String email;
    private Boolean enabled;
    private List<String> roles;

    public Whoami(final User user) {
        this.roles = user.getPresentRoles();
        this.email = user.getEmail();
        this.id = user.getId();
        this.login = user.getLogin();
        this.enabled = user.getEnabled();
    }
}
