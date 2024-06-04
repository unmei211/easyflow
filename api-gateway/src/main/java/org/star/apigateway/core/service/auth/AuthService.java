package org.star.apigateway.core.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.entity.password.Password;
import org.star.apigateway.core.entity.roles.Role;
import org.star.apigateway.core.entity.roles.RolesEnum;
import org.star.apigateway.core.entity.user.UserAuth;
import org.star.apigateway.core.factory.EntityFactory;
import org.star.apigateway.core.factory.UserAuthFactory;
import org.star.apigateway.core.repository.auth.ReactiveAuthRepository;
import org.star.apigateway.core.repository.password.ReactivePasswordRepository;
import org.star.apigateway.core.repository.role.ReactiveRoleRepository;
import org.star.apigateway.core.security.encrypt.BCryptPasswordEncoder;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.microservice.service.social.client.webclient.SocialServiceWebClient;
import org.star.apigateway.microservice.service.user.client.webclient.UserServiceWebClient;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.web.model.jwt.TokensBundle;
import org.star.apigateway.web.model.user.UserViaRoles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final EntityFactory<UserAuth.UserAuthBuilder> userFactory;
    private final EntityFactory<Password.PasswordBuilder> passwordFactory;
    //    private final UserAuthRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ReactiveRoleRepository roleRepository;
    private final JwtService jwtService;
    private final UserServiceWebClient userServiceAsyncClient;
    private final SocialServiceWebClient socialServiceWebClient;
    private final ReactiveAuthRepository authRepository;
    private final ReactivePasswordRepository passwordRepository;

    private static void logServiceLayerProcessed(
            final Class<? extends RuntimeException> exceptionClazz,
            final Class<?> serviceClazz
    ) {
        log.info(
                "The error: [{}]\thas been processed in the service: [{}]",
                exceptionClazz.getSimpleName(),
                serviceClazz.getSimpleName()
        );
    }

    public Mono<UserViaId> register(
            final String login,
            final String email,
            final String password
    ) {
        return userServiceAsyncClient.saveUser(new UserToSaveTransfer(
                        login,
                        email
                ))
                .onErrorMap(ForbiddenException.class, e -> {
                    logServiceLayerProcessed(e.getClass(), this.getClass());
                    return new ForbiddenException("Can't save user in user service");
                })
                .flatMap(user -> {
                    Mono<Void> authSaveMono = authRepository.save(userFactory.toNew(user.getUserId()).build()).then();
                    Mono<Void> passwordSaveMono = passwordRepository.save(
                            passwordFactory.toNew(user.getUserId()).password(encoder.encrypt(password)).build()
                    ).then();
                    Mono<Void> socialInitialMono = socialServiceWebClient.createSocial(user);
                    Mono<Void> bindWithRoleMono = roleRepository.bindNewUserWith(user.getUserId(), RolesEnum.USER.toString());

                    Flux<Void> transactions = Flux.concat(
                            authSaveMono,
                            passwordSaveMono,
                            socialInitialMono,
                            bindWithRoleMono
                    );

                    return transactions.then(Mono.just(user));
                });
    }

    public Mono<TokensBundle> login(
            final String login,
            final String password
    ) {
        return userServiceAsyncClient.findUserByLogin(login)
                .flatMap(userViaInfo -> authRepository
                        .findById(userViaInfo.getId())
                        .switchIfEmpty(Mono.error(new NotFoundException("User via id not found in auth"))))
                .flatMap(userAuth -> {
                    if (!userAuth.getEnabled()) {
                        return Mono.error(new ForbiddenException("User not authorize"));
                    }
                    return passwordRepository.findById(userAuth.getId())
                            .flatMap(passFromDb -> {
                                if (!encoder.matches(password, passFromDb.getPassword())) {
                                    return Mono.error(new ForbiddenException("Wrong password"));
                                }
                                return Mono.just(userAuth);
                            })
                            .flatMap(userAuthInner -> roleRepository.findUserRoles(userAuthInner.getId())
                                    .collectList()
                                    .flatMap(roles -> {
                                        UserViaRoles userViaRoles = new UserViaRoles(
                                                userAuthInner.getId(),
                                                roles.stream().map(Role::getRole).toList()
                                        );
                                        String accessToken = jwtService.createAccessToken(userViaRoles);
                                        Mono<String> refreshToken = jwtService.upsertRefreshToken(userViaRoles);
                                        return refreshToken.map(s -> new TokensBundle(accessToken, s));
                                    }));
                });
//                .flatMap(passFromDb -> {
//                    if(!encoder.matches(password, passFromDb.getPassword())) {
//                        return Mono.error(new ForbiddenException("Wrong password"));
//                    }
//                    roleRepository.findUserRoles()
//                    jwtService.createAccessToken()
//                })
    }

//
//    public Mono<ResponseEntity<TokensBundle>> login(
//            final String login,
//            final String password
//    ) {
//        return userServiceAsyncClient.findUserByLogin(login)
//                .onErrorMap(
//                        NotFoundException.class,
//                        e -> {
//                            logServiceLayerProcessed(e.getClass(), this.getClass());
//                            return new ForbiddenException("Didn't find a user via login");
//                        }
//                )
//                .map(userViaInfo -> {
//                    log.info("Get userViaInfo {}", userViaInfo.toString());
//
//                    Optional<UserAuth> user = userRepository.findById(userViaInfo.getId());
//                    if (user.isEmpty()) {
//                        log.info("User not found");
//                        throw new NotFoundException("User not found");
//                    }
//                    if (!user.get().getEnabled()) {
//                        log.info("User is disabled");
//                        throw new ForbiddenException("User not enabled");
//                    }
//
//                    if (!encoder.matches(password, user.get().getPassword().getValue())) {
//                        log.info("Password not matches");
//                        throw new ForbiddenException("Password not matches");
//                    }
//
//
//                    TokensBundle bundle = new TokensBundle();
//                    String accessToken = jwtService.createAccessToken(user.get());
//                    log.info("try bundle token access");
//                    bundle.setAccessToken(jwtService.createAccessToken(user.get()));
//                    log.info("bundled token access");
//                    bundle.setRefreshToken(jwtService.upsertRefreshToken(user.get()));
//
//                    return ResponseEntity.ok(bundle);
//                })
//                .onErrorMap(throwable -> {
//                    return throwable;
//                });
//    }
//
//    public UserAuth findById(final String userId) {
//        return userRepository.findById(userId).orElseThrow(() -> {
//                    log.error("not found user {}", userId);
//                    return new NotFoundException("User with id " + userId + " not found");
//                }
//        );
//    }
}
