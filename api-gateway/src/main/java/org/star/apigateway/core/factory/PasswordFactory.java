package org.star.apigateway.core.factory;

import org.star.apigateway.core.entity.password.Password;

public class PasswordFactory implements EntityFactory<Password.PasswordBuilder> {
    @Override
    public Password.PasswordBuilder toNew() {
        return Password.builder().isNew(true);
    }

    @Override
    public Password.PasswordBuilder toNew(String newId) {
        return toNew().userId(newId);
    }

    @Override
    public Password.PasswordBuilder toExists(String existsEntityId) {
        return Password.builder().isNew(false).userId(existsEntityId);
    }
}
