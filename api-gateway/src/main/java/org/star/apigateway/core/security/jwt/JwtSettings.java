package org.star.apigateway.core.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * JwtSettings
 */
@Component
public class JwtSettings {
    @Getter
    private final String tokenIssuer;
    private final String tokenSigningKey;
    private final int aTokenDuration;
    private final int aRefreshTokenDuration;

    /**
     * From prop
     * @param tokenIssuer issuer
     * @param tokenSigningKey key
     * @param aTokenDuration duration
     * @param aRefreshTokenDuration refresh token duration
     */
    public JwtSettings(
            @Value("${jwt.issuer}") final String tokenIssuer,
            @Value("${jwt.signingKey}") final String tokenSigningKey,
            @Value("${jwt.aTokenDuration}") final int aTokenDuration,
            @Value("${jwt.rTokenDuration}") final int aRefreshTokenDuration
    ) {
        this.tokenIssuer = tokenIssuer;
        this.aTokenDuration = aTokenDuration;
        this.tokenSigningKey = tokenSigningKey;
        this.aRefreshTokenDuration = aRefreshTokenDuration;
    }

    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    public Duration getTokenExpirationIn() {
        return Duration.ofMinutes(aTokenDuration);
    }

    public Duration getRefreshTokenExpirationIn() {
        return Duration.ofHours(aRefreshTokenDuration);
    }
}
