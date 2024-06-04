package org.star.apigateway.web.controller.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.star.apigateway.core.entity.token.RefreshToken;
import org.star.apigateway.core.repository.token.ReactiveTokenRepository;
import org.star.apigateway.core.service.auth.AuthService;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.microservice.service.user.client.feignclient.UserServiceFeignClient;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
import org.star.apigateway.web.model.auth.Login;
import org.star.apigateway.web.model.auth.Registration;
import org.star.apigateway.web.model.jwt.TokensBundle;
import org.star.apigateway.web.model.user.UserViaRoles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final ReactiveTokenRepository tokenRepository;

    @PostMapping("/login")
    public Mono<ResponseEntity<TokensBundle>> login(
            final @RequestBody Login login
    ) {
        return authService.login(login.getLogin(), login.getPassword()).map(ResponseEntity::ok);
    }

//    @AuthRoleRequired(anyRole = true)
//    @GetMapping("/whoami")
//    public ResponseEntity<?> whoami(
//            UserCredentials userCredentials
//    ) {
//        UserAuth user = authService.findById(userCredentials.getUserId());
//        return new ResponseEntity<>(UserAuthPublic.build(user), HttpStatus.OK);
//    }

    @PostMapping
    public Mono<ResponseEntity<?>> register(
            final @RequestBody Registration registration
    ) {
        return authService.register(
                registration.getLogin(),
                registration.getEmail(),
                registration.getPassword()
        ).thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping
    public Mono<?> test() {
        return tokenRepository.
                save(RefreshToken.builder()
                        .refreshToken("fgfd").isNew(true).userId("23256541-b2b7-47af-beac-384e3ed3ffbe").build()
                ).map(refreshToken -> {
                    System.out.println("wtf");
                    return refreshToken;
                });
//        jwtService.upsertRefreshToken(new UserViaRoles("23256541-b2b7-47af-beac-384e3ed3ffbe", List.of()));
    }
}
