package org.star.apigateway.core.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.entity.token.RefreshToken;
import org.star.apigateway.core.entity.user.UserAuth;
import org.star.apigateway.core.repository.token.TokenRepository;
import org.star.apigateway.core.repository.user.UserAuthRepository;
import org.star.apigateway.core.security.jwt.JwtSettings;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.apigateway.web.exception.security.TokenExpiredException;
import org.star.apigateway.web.model.jwt.TokensBundle;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {
    private final JwtSettings settings;
    private final TokenRepository tokenRepository;
    private final UserAuthRepository userAuthRepository;
    private final String ROLES = "roles";

    public String createAccessToken(final UserAuth user) {
        Instant now = Instant.now();
        log.info("try create access token for user {}", user.getId());
        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getId())
                .setExpiration(Date.from(now.plus(settings.getTokenExpirationIn())));
        System.out.println(user.getRoles());
        claims.put(ROLES, user.getPresentRoles());
        log.info("claims created");
        log.info("user roles {}", user.getRoles().get(0).getRole());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    public String createRefreshToken(final UserAuth user) {
        Instant now = Instant.now();

        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getId())
                .setExpiration(Date.from(
                        now.plus(settings.getRefreshTokenExpirationIn())
                ));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
    }

    public boolean isExpired(final Jws<Claims> claims) {
        Date now = new Date();
        return claims.getBody().getExpiration().before(now);
    }

    @SuppressWarnings("unchecked")
    public UserCredentials parseToken(final String token) {
        log.info("try parse token {}", token);
        Jws<Claims> claims;
        claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);
        log.info("parsed {}", claims);
        if (isExpired(claims)) {
            log.error("token is expired");
            throw new TokenExpiredException("tokes is expired");
        }

        String subject = claims.getBody().getSubject();
        log.info("subject {}", subject);

        List<String> roles = claims.getBody().get(ROLES, List.class);
        log.info("roles {}", roles.toString());
        return new UserCredentials(subject, roles);
    }

    public String parseRefreshToken(final String refreshToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(refreshToken);

        if (isExpired(claims)) {
            throw new TokenExpiredException("tokes is expired");
        }

        return claims.getBody().getSubject();
    }

    public String upsertRefreshToken(final UserAuth user) {
        String refreshTokenRaw = createRefreshToken(user);

        Optional<RefreshToken> refreshToken = tokenRepository.findByUserId(user.getId());
        if (refreshToken.isEmpty()) {
            refreshToken = Optional.of(new RefreshToken());
            refreshToken.get().setUser(user);
        }
        refreshToken.get().setToken(refreshTokenRaw);

        tokenRepository.save(refreshToken.get());

        return refreshTokenRaw;
    }

    public TokensBundle refreshTokens(final String oldRefreshToken) {
        if (!tokenRepository.existsByToken(oldRefreshToken)) {
            throw new NotFoundException("token " + oldRefreshToken + " not found");
        }

        String userId = parseRefreshToken(oldRefreshToken);

        UserAuth user = userAuthRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user not found " + userId)
        );

        String refreshToken = createRefreshToken(user);

        tokenRepository.updateRefreshToken(refreshToken, userId);

        String accessToken = createAccessToken(user);

        return new TokensBundle(accessToken, refreshToken);
    }
}
