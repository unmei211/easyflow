package org.star.apigateway.core.service.auth;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.model.user.UserAuth;
import org.star.apigateway.core.repository.role.RoleRepository;
import org.star.apigateway.core.repository.user.UserAuthRepository;
import org.star.apigateway.core.security.encrypt.BCryptPasswordEncoder;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.microservice.share.error.exceptions.core.ServiceUnavailable;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.feignclient.UserServiceFeignClient;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.service.user.webclient.UserServiceWebClient;
import org.star.apigateway.web.model.jwt.TokensBundle;

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
    private final UserServiceWebClient userServiceAsync;

    public void register(
            final String login,
            final String email,
            final String password
    ) {
        System.out.println("BEBEBEBEBE");
//        UserViaId userViaId = Optional.of(userService.saveUser(new UserToSaveTransfer(login, email))).orElseThrow(
//                () -> new ServiceUnavailable("Service unavailable")
//        );

        userServiceAsync.saveUserAsync(new UserToSaveTransfer(login, email))
                .subscribe(
                        userViaId -> log.info("get user" + userViaId.getUserId()),
                        exception -> {
                            ер
                        }
                );

//        final String hashPassword = encoder.encrypt(password);
//        Role role = roleRepository.findByRole(RolesEnum.USER.toString()).orElseThrow(
//                () -> new NotFoundException("role with provided name not found")
//        );
//        UserAuth user = new UserAuth(userViaId.getUserId());
//        user.getRoles().add(role);
//        user.setPassword(new Password(hashPassword, user));
//        log.info("save user {}", user.getId());
//        userRepository.save(user);
    }

    public TokensBundle login(
            final String login,
            final String password
    ) {
        log.info("login user");
        UserAuth userAuth;
        try {
            UserViaId userViaId = null;
            Optional.of(userService.findUserByLogin(login)).orElseThrow(
                    () -> new ServiceUnavailable("Service unavailable")
            );
            userAuth = userRepository.findById(userViaId.getUserId()).orElseThrow(
                    () -> new ForbiddenException("User not found")
            );
        } catch (FeignException e) {
            throw new ServiceUnavailable("Service unavailable");
        }

        System.out.println(userAuth.getRoles());
        if (!userAuth.isEnabled()) {
            throw new ForbiddenException("user with login: " + login + " ban");
        }

        if (!encoder.matches(password, userAuth.getPassword().getValue())) {
            throw new UnauthorizedException("pass not matches");
        }

        TokensBundle bundle = new TokensBundle();
        String accessToken = jwtService.createAccessToken(userAuth);
        log.info("try bundle token access");
        bundle.setAccessToken(jwtService.createAccessToken(userAuth));
        log.info("bundled token access");
        bundle.setRefreshToken(jwtService.upsertRefreshToken(userAuth));

        return bundle;
    }
}
