package org.star.apigateway.web.controller.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.star.apigateway.core.model.user.UserAuth;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;
import org.star.apigateway.core.security.resolver.AuthRoleRequired;
import org.star.apigateway.core.security.user.UserCredentials;
import org.star.apigateway.core.service.auth.AuthService;
import org.star.apigateway.core.service.auth.DataAuthService;
import org.star.apigateway.microservice.service.user.feignclient.UserServiceFeignClient;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.web.model.auth.Login;
import org.star.apigateway.web.model.auth.Registration;
import org.star.apigateway.web.model.jwt.TokensBundle;
import org.star.apigateway.web.model.user.UserAuthPublic;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final DataAuthService dataAuthService;
    private final UserServiceFeignClient feignClient;
    private final ReactiveJwtInterceptor reactiveJwtInterceptor;

    @PostMapping
    public Mono<ResponseEntity<Object>> register(
            final @RequestBody Registration registration
    ) {
        return authService.register(registration.getLogin(), registration.getEmail(), registration.getPassword())
                .map(userViaId -> {
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                })
//                .flatMap(userViaId -> {
//                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).build());
//                })
//                .onErrorResume((throwable -> {
//                    return Mono.just(ResponseEntity.of(Optional.of(throwable)));
//                }));
                .onErrorReturn(ForbiddenException.class,
                        ResponseEntity.status(HttpStatus.FORBIDDEN).build()
                );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(
            final @RequestBody Login login
    ) {
       return authService.login(login.getLogin(), login.getPassword())
               .flatMap(tokensBundle -> {
                   // Создаем новый ResponseEntity с объектом tokensBundle в качестве тела ответа
                   return Mono.just(ResponseEntity.ok((Object) tokensBundle));
               })// Преобразовать TokensBundle в ResponseEntity
               .onErrorResume(NotFoundException.class, error -> {
                   return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
               })
               .onErrorResume(ForbiddenException.class,
                       error -> Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build()));

//        return new ResponseEntity<>(bundle, HttpStatus.OK);
    }

    @AuthRoleRequired(anyRole = true)
    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(
            ServerWebExchange exchange

    ) {
        UserCredentials userCredentials = reactiveJwtInterceptor.getUserCredentials(exchange);
        if (userCredentials == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        UserAuth user = dataAuthService.findById(userCredentials.getUserId());

        return new ResponseEntity<>(UserAuthPublic.build(user), HttpStatus.OK);
    }
}
