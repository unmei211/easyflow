package org.star.apigateway.web.model.user;

import lombok.Getter;
import org.star.apigateway.core.entity.user.UserAuth;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAuthPublic {
//    private final String id;
    private final Boolean enabled;
//    private final List<String> roles;

    public UserAuthPublic(final UserAuth user) {
//        this.roles = user.getPresentRoles();
//        this.id = user.getId();
        this.enabled = user.getEnabled();
    }

    public static UserAuthPublic build(
            final UserAuth user
    ) {
        return new UserAuthPublic(user);
    }

    public static List<UserAuthPublic> build(
            final List<UserAuth> users
    ) {
        List<UserAuthPublic> pubs = new ArrayList<>();
        for (final UserAuth user: users) {
            pubs.add(new UserAuthPublic(user));
        }
        return pubs;
    }
}
