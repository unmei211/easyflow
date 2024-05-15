package org.star.apigateway.core.service.auth;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.star.apigateway.core.entity.password.Password;
import org.star.apigateway.core.entity.roles.Role;
import org.star.apigateway.core.entity.roles.RolesEnum;
import org.star.apigateway.core.entity.user.UserAuth;
import org.star.apigateway.core.repository.role.RoleRepository;
import org.star.apigateway.core.repository.user.UserAuthRepository;
import org.star.apigateway.core.security.encrypt.BCryptPasswordEncoder;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.microservice.service.social.client.webclient.SocialServiceWebClient;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.service.user.client.webclient.UserServiceWebClient;
import org.star.apigateway.web.model.jwt.TokensBundle;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserAuthRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    //    private final UserServiceFeignClient userService;
    private final UserServiceWebClient userServiceAsyncClient;
    private final SocialServiceWebClient socialServiceWebClient;
//    private final ErrorsAssociate associate;

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

    public Mono<ResponseEntity<UserViaId>> register(
            final String login,
            final String email,
            final String password
    ) {
        Mono<UserViaId> createUserMono = userServiceAsyncClient.saveUser(new UserToSaveTransfer(login, email))
                .onErrorMap(ForbiddenException.class, e -> {
                    logServiceLayerProcessed(e.getClass(), this.getClass());
                    return new ForbiddenException("Error in userServiceAsync");
                })
                .map(userViaId -> {
                    log.info("Id to save {}", userViaId.getUserId());

                    if (userRepository.findById(userViaId.getUserId()).isPresent()) {
                        log.info("User via id exist {}", userViaId.getUserId());
                        throw new ForbiddenException();
                    }

                    final String hashPassword = encoder.encrypt(password);

                    Role role = roleRepository.findByRole(RolesEnum.USER.toString()).orElseThrow(
                            () -> {
                                log.info("Not found Role");
                                return new NotFoundException("Not found role");
                            }
                    );

                    UserAuth newUserAuth = new UserAuth();
                    newUserAuth.setId(userViaId.getUserId());
                    newUserAuth.setRoles(List.of(role));
                    newUserAuth.setPassword(new Password(hashPassword, newUserAuth));
                    userRepository.save(newUserAuth);
                    log.info("User is saved");
                    return userViaId;
                })
                .onErrorMap(ForbiddenException.class, e -> {
                    logServiceLayerProcessed(e.getClass(), this.getClass());
                    return new ForbiddenException("User via this pass already exist");
                });
        return createUserMono.flatMap(
                user -> {
                    log.info("Complete save user ");
                    Mono<Void> createSocialMono = socialServiceWebClient.createSocial(user);
                    log.info("Check");
//                    Mono<Void> createSocialMono = Mono.empty();
                    return createSocialMono
                            .onErrorMap(ForbiddenException.class, e -> {
                                logServiceLayerProcessed(e.getClass(), this.getClass());
                                return new ForbiddenException("Error in userServiceAsync");
                            })
                            .thenReturn(new ResponseEntity<>(user, HttpStatus.CREATED));
                }
        );


//        Mono<Tuple2<Integer, UserViaId>> zip;
//        Mono<Integer> v = Mono.just(5);
//
//        Mono<Void> createSocialMono = socialServiceWebClient.createSocial()
//
//        return Mono.zip(v, createUserMono).map(
//                tuple -> {
//                    System.out.println(tuple.getT1());
//                    return new ResponseEntity<>(tuple.getT2(), HttpStatus.CREATED);
//                }
//        );


//        return createUserMono.map(
//                user -> {
//                    return new ResponseEntity<>(user, HttpStatus.CREATED);
//                }
//        );
//        return userServiceAsyncClient.saveUser(new UserToSaveTransfer(login, email))
//                .onErrorMap(ForbiddenException.class, e -> {
//                    logServiceLayerProcessed(e.getClass(), this.getClass());
//                    return new ForbiddenException("Error in userServiceAsync");
//                })
//                .map(userViaId -> {
//                    log.info("Id to save {}", userViaId.getUserId());
//
//                    if (userRepository.findById(userViaId.getUserId()).isPresent()) {
//                        log.info("User via id exist {}", userViaId.getUserId());
//                        throw new ForbiddenException();
//                    }
//
//                    final String hashPassword = encoder.encrypt(password);
//
//                    Role role = roleRepository.findByRole(RolesEnum.USER.toString()).orElseThrow(
//                            () -> {
//                                log.info("Not found Role");
//                                return new NotFoundException("Not found role");
//                            }
//                    );
//
//                    UserAuth newUserAuth = new UserAuth();
//                    newUserAuth.setId(userViaId.getUserId());
//                    newUserAuth.setRoles(List.of(role));
//                    newUserAuth.setPassword(new Password(hashPassword, newUserAuth));
//                    userRepository.save(newUserAuth);
//                    return userViaId;
////                    return new ResponseEntity<>(userViaId, HttpStatus.CREATED);
//                })
//                .onErrorMap(ForbiddenException.class, e -> {
//                    logServiceLayerProcessed(e.getClass(), this.getClass());
//                    return new ForbiddenException("User via this pass already exist");
//                });
    }

    public Mono<ResponseEntity<TokensBundle>> login(
            final String login,
            final String password
    ) {
        return userServiceAsyncClient.findUserByLogin(login)
                .onErrorMap(
                        NotFoundException.class,
                        e -> {
                            logServiceLayerProcessed(e.getClass(), this.getClass());
                            return new ForbiddenException("Didn't find a user via login");
                        }
                )
                .map(userViaInfo -> {
                    log.info("Get userViaInfo {}", userViaInfo.toString());

                    Optional<UserAuth> user = userRepository.findById(userViaInfo.getId());
                    if (user.isEmpty()) {
                        log.info("User not found");
                        throw new NotFoundException("User not found");
                    }
                    if (!user.get().getEnabled()) {
                        log.info("User is disabled");
                        throw new ForbiddenException("User not enabled");
                    }

                    if (!encoder.matches(password, user.get().getPassword().getValue())) {
                        log.info("Password not matches");
                        throw new ForbiddenException("Password not matches");
                    }


                    TokensBundle bundle = new TokensBundle();
                    String accessToken = jwtService.createAccessToken(user.get());
                    log.info("try bundle token access");
                    bundle.setAccessToken(jwtService.createAccessToken(user.get()));
                    log.info("bundled token access");
                    bundle.setRefreshToken(jwtService.upsertRefreshToken(user.get()));

                    return ResponseEntity.ok(bundle);
                })
                .onErrorMap(throwable -> {
                    return throwable;
                });
    }
}
