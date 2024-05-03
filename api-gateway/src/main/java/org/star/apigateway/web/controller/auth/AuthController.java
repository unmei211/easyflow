package org.star.apigateway.web.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.star.apigateway.core.model.user.User;
import org.star.apigateway.core.security.jwt.ReactiveJwtInterceptor;
import org.star.apigateway.core.security.user.UserCredentials;
import org.star.apigateway.core.service.auth.AuthService;
import org.star.apigateway.web.exception.security.UnauthorizedException;
import org.star.apigateway.web.model.auth.Login;
import org.star.apigateway.web.model.auth.Registration;
import org.star.apigateway.web.model.jwt.TokensBundle;
import org.star.apigateway.web.model.user.Whoami;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ReactiveJwtInterceptor reactiveJwtInterceptor;

    @PostMapping
    public ResponseEntity<?> register(
            final @RequestBody Registration registration
    ) {
        System.out.println(registration.getLogin());
        authService.register(
                registration.getLogin(),
                registration.getEmail(),
                registration.getPassword()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            final @RequestBody Login login
    ) {
        TokensBundle bundle = authService.login(login.getLogin(), login.getPassword());
        return new ResponseEntity<>(bundle, HttpStatus.OK);
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(
            ServerWebExchange exchange

    ) {
        UserCredentials userCredentials = reactiveJwtInterceptor.getUserCredentials(exchange);
        if (userCredentials == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        User user = authService.findById(userCredentials.getUserId());

        return new ResponseEntity<>(Whoami.build(user), HttpStatus.OK);
    }
}
