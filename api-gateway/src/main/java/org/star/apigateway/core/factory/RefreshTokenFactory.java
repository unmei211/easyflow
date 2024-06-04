package org.star.apigateway.core.factory;

import org.star.apigateway.core.entity.token.RefreshToken;

public class RefreshTokenFactory implements EntityFactory<RefreshToken.RefreshTokenBuilder> {
    @Override
    public RefreshToken.RefreshTokenBuilder toNew() {
        return RefreshToken.builder().isNew(true);
    }

    @Override
    public RefreshToken.RefreshTokenBuilder toNew(String newId) {
        return RefreshToken.builder().isNew(true).refreshToken(newId);
    }

    @Override
    public RefreshToken.RefreshTokenBuilder toExists(String existsEntityId) {
        return RefreshToken.builder().isNew(false).refreshToken(existsEntityId);
    }
}
