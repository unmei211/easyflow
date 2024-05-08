package org.star.apigateway.core.service.auth;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.model.enabled.UserEnabled;
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
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserAuthRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final UserServiceFeignClient userService;
    private final UserServiceWebClient userServiceAsyncClient;
    private final ErrorsAssociate associate;

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
        return userServiceAsyncClient.saveUserAsync(new UserToSaveTransfer(login, email))
                .map(userViaId -> {
                    final String hashPassword = encoder.encrypt(password);

                    Role role = roleRepository.findByRole(RolesEnum.USER.toString()).orElseThrow(
                            () -> new NotFoundException("Not found role")
                    );

                    UserAuth newUserAuth = new UserAuth();
                    newUserAuth.setId(userViaId.getUserId());
                    newUserAuth.setRoles(List.of(role));
                    newUserAuth.setEnabled(new UserEnabled(newUserAuth));
                    newUserAuth.setPassword(new Password(hashPassword, newUserAuth));
                    userRepository.save(newUserAuth);
                    return userViaId;
                })
                .onErrorMap(ForbiddenException.class, error -> {
                    logServiceLayerProcessed(ForbiddenException.class, AuthService.class);
                    return error;
//                    return associate.getInit("FORBIDDEN", HttpStatus.FORBIDDEN);
                });
    }

    public Mono<TokensBundle> login(
            final String login,
            final String password
    ) {
        return userServiceAsyncClient.findUserByLoginAsync(login)
                .flatMap(userViaInfo -> {
                    System.out.println("id" + userViaInfo.getId());
                    Optional<UserAuth> user = userRepository.findById(userViaInfo.getId());
                    if (user.isEmpty()) {
                        return Mono.error(new NotFoundException("User not found"));
                    }
                    if (!user.get().isEnabled()) {
                        return Mono.error(new ForbiddenException("User not enabled"));
                    }

                    if (!encoder.matches(password, user.get().getPassword().getValue())) {
                        return Mono.error(new ForbiddenException("Password not matches"));
                    }


                    TokensBundle bundle = new TokensBundle();
                    String accessToken = jwtService.createAccessToken(user.get());
                    log.info("try bundle token access");
                    bundle.setAccessToken(jwtService.createAccessToken(user.get()));
                    log.info("bundled token access");
                    bundle.setRefreshToken(jwtService.upsertRefreshToken(user.get()));

                    return Mono.just(bundle);
                });
//
//
//        log.info("login user");
//        UserAuth userAuth;
//        try {
//            UserViaId userViaId = null;
//            Optional.of(userService.findUserByLogin(login)).orElseThrow(
//                    () -> new ServiceUnavailable("Service unavailable")
//            );
//            userAuth = userRepository.findById(userViaId.getUserId()).orElseThrow(
//                    () -> new ForbiddenException("User not found")
//            );
//        } catch (FeignException e) {
//            throw new ServiceUnavailable("Service unavailable");
//        }
//
//        System.out.println(userAuth.getRoles());
//        if (!userAuth.isEnabled()) {
//            throw new ForbiddenException("user with login: " + login + " ban");
//        }
//
//        if (!encoder.matches(password, userAuth.getPassword().getValue())) {
//            throw new UnauthorizedException("pass not matches");
//        }
//
//        TokensBundle bundle = new TokensBundle();
//        String accessToken = jwtService.createAccessToken(userAuth);
//        log.info("try bundle token access");
//        bundle.setAccessToken(jwtService.createAccessToken(userAuth));
//        log.info("bundled token access");
//        bundle.setRefreshToken(jwtService.upsertRefreshToken(userAuth));
//
//        return bundle;
    }
}
