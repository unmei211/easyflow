package org.star.apigateway.core.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.entity.roles.Role;
import org.star.apigateway.core.entity.token.RefreshToken;
import org.star.apigateway.core.entity.user.UserAuth;
import org.star.apigateway.core.factory.EntityFactory;
import org.star.apigateway.core.repository.auth.ReactiveAuthRepository;
import org.star.apigateway.core.repository.role.ReactiveRoleRepository;
import org.star.apigateway.core.repository.token.ReactiveTokenRepository;
import org.star.apigateway.core.security.jwt.JwtSettings;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.apigateway.web.exception.security.TokenExpiredException;
import org.star.apigateway.web.model.jwt.TokensBundle;
import org.star.apigateway.web.model.user.UserViaRoles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class JwtService {
    private final JwtSettings settings;
    private final ReactiveTokenRepository tokenRepository;
    private final ReactiveRoleRepository roleRepository;
    private final ReactiveAuthRepository authRepository;
    private final EntityFactory<RefreshToken.RefreshTokenBuilder> tokenFactory;
    //    private final UserAuthRepository userAuthRepository;
    private final String ROLES = "roles";

    public String createAccessToken(final UserViaRoles user) {
        Instant now = Instant.now();
        log.info("try create access token for user {}", user.getId());
        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getId())
                .setExpiration(Date.from(now.plus(settings.getTokenExpirationIn())));
        claims.put(ROLES, user.getRoles());
        log.info("claims created");
        log.info("user roles {}", user.getRoles().get(0));
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    public String createRefreshToken(final UserViaRoles user) {
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

    public Mono<String> parseRefreshToken(final String refreshToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(refreshToken);

        if (isExpired(claims)) {
            throw new TokenExpiredException("tokes is expired");
        }

        return Mono.just(claims.getBody().getSubject());
    }

    public Mono<String> upsertRefreshToken(final UserViaRoles user) {
        String refreshTokenRaw = createRefreshToken(user);
        Mono<Boolean> tokenExistMono = tokenRepository.existsByUserId(user.getId());

        return tokenExistMono
                .flatMap(tokenExist -> {
                    if (tokenExist) {
                        log.info("Token exist, update to new");
                        return tokenRepository.updateRefreshToken(refreshTokenRaw, user.getId()).thenReturn(refreshTokenRaw);
                    } else {
                        log.info("Token didn't exists for user {}, update and create to new {}", user.getId(), refreshTokenRaw);
                        return tokenRepository.save(
                                tokenFactory.toNew(refreshTokenRaw)
                                        .userId(user.getId())
                                        .build()).map(RefreshToken::getRefreshToken);
                    }
                });
    }

    public Mono<TokensBundle> refreshTokens(final String oldRefreshToken) {
        Mono<Boolean> tokenExist = tokenRepository.existsById(oldRefreshToken);
        Mono<String> userIdFromTokenMono = parseRefreshToken(oldRefreshToken);
        Mono<UserAuth> userFromRepoMono = authRepository
                .findById(oldRefreshToken)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found user")));

        return Mono.zip(tokenExist, userIdFromTokenMono, userFromRepoMono)
                .flatMap(tuple -> {
                    if (!tuple.getT1()) {
                        return Mono.error(new ForbiddenException("Can't refresh token because didn't find token"));
                    }
                    String userId = tuple.getT2();

                    return roleRepository
                            .findUserRoles(userId)
                            .collectList()
                            .flatMap(roles -> {
                                UserViaRoles userViaRoles = new UserViaRoles(userId, roles.stream().map(Role::getRole).toList());
                                String refreshToken = createRefreshToken(userViaRoles);
                                String accessToken = createAccessToken(userViaRoles);

                                Mono<Void> updateTokenMono = tokenRepository.updateRefreshToken(refreshToken, userId);

                                return updateTokenMono.thenReturn(new TokensBundle(accessToken, refreshToken));
                            });
                });
//        if (!tokenRepository.existsByToken(oldRefreshToken)) {
//            throw new NotFoundException("token " + oldRefreshToken + " not found");
//        }

//        String userId = parseRefreshToken(oldRefreshToken);

//        UserAuth user = userAuthRepository.findById(userId).orElseThrow(
//                () -> new NotFoundException("user not found " + userId)
//        );

//        String refreshToken = createRefreshToken(user);
//
//        tokenRepository.updateRefreshToken(refreshToken, userId);
//
//        String accessToken = createAccessToken(user);
//
//        return new TokensBundle(accessToken, refreshToken);
    }
}
