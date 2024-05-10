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
    public Mono<ResponseEntity<UserViaId>> register(
            final @RequestBody Registration registration
    ) {
        return authService.register(
                registration.getLogin(),
                registration.getEmail(),
                registration.getPassword()
        );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokensBundle>> login(
            final @RequestBody Login login
    ) {
        return authService.login(login.getLogin(), login.getPassword());
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
