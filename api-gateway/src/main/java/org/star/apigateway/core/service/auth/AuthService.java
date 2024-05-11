package org.star.apigateway.core.service.auth;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.model.password.Password;
import org.star.apigateway.core.model.roles.Role;
import org.star.apigateway.core.model.roles.RolesEnum;
import org.star.apigateway.core.model.user.UserAuth;
import org.star.apigateway.core.repository.role.RoleRepository;
import org.star.apigateway.core.repository.user.UserAuthRepository;
import org.star.apigateway.core.security.encrypt.BCryptPasswordEncoder;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.core.ServiceUnavailable;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.feignclient.UserServiceFeignClient;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.service.user.webclient.UserServiceWebClient;
import org.star.apigateway.web.model.jwt.TokensBundle;
import reactor.core.publisher.Mono;

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
        return userServiceAsyncClient.saveUser(new UserToSaveTransfer(login, email))
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
                    return new ResponseEntity<>(userViaId, HttpStatus.CREATED);
                })
                .onErrorMap(ForbiddenException.class, e -> {
                    logServiceLayerProcessed(e.getClass(), this.getClass());
                    return new ForbiddenException("User via this pass already exist");
                });
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
