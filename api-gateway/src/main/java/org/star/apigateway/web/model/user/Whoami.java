package org.star.apigateway.web.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.star.apigateway.core.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class Whoami {
    private final String id;
    private final String login;
    private final String email;
    private final Boolean enabled;
    private final List<String> roles;

    public Whoami(final User user) {
        this.roles = user.getPresentRoles();
        this.id = user.getId();
        this.login = user.getLogin();
        this.enabled = user.getEnabled();
        this.email = user.getEmail();
    }

    public static Whoami build(
            final User user
    ) {
        return new Whoami(user);
    }
}
