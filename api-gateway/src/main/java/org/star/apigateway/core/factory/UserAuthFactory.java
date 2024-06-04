package org.star.apigateway.core.factory;

import jakarta.validation.constraints.NotNull;
import org.star.apigateway.core.entity.user.UserAuth;

import java.util.UUID;

public class UserAuthFactory implements EntityFactory<UserAuth.UserAuthBuilder> {

    public UserAuth.UserAuthBuilder toNew() {
        return UserAuth.builder().isNew(true).id(UUID.randomUUID().toString());
    }

    public UserAuth.UserAuthBuilder toNew(@NotNull String newUserId) {
        return UserAuth.builder().isNew(true).id(newUserId);
    }

    public UserAuth.UserAuthBuilder toExists(@NotNull final String existsUserId) {
        return UserAuth.builder().isNew(false).id(existsUserId);
    }
}
