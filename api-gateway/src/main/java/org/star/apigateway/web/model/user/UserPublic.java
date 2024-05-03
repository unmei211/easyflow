package org.star.apigateway.web.model.user;

import lombok.Getter;
import org.star.apigateway.core.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserPublic {
    private final String id;
    private final String login;
    private final Boolean enabled;
    private final List<String> roles;

    public UserPublic(final User user) {
        this.roles = user.getPresentRoles();
        this.id = user.getId();
        this.login = user.getLogin();
        this.enabled = user.getEnabled();
    }

    public static UserPublic build(
            final User user
    ) {
        return new UserPublic(user);
    }

    public static List<UserPublic> build(
            final List<User> users
    ) {
        List<UserPublic> pubs = new ArrayList<>();
        for (final User user: users) {
            pubs.add(new UserPublic(user));
        }
        return pubs;
    }
}
