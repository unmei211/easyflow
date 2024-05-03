package org.star.apigateway.core.service.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.model.roles.Role;
import org.star.apigateway.core.model.roles.RolesEnum;
import org.star.apigateway.core.model.user.User;
import org.star.apigateway.core.repository.UserRepository;
import org.star.apigateway.core.repository.role.RoleRepository;
import org.star.apigateway.core.security.encrypt.BCryptPasswordEncoder;
import org.star.apigateway.core.service.security.JwtService;
import org.star.apigateway.web.exception.core.EntityExistException;
import org.star.apigateway.web.exception.core.NotFoundException;
import org.star.apigateway.web.exception.security.UnauthorizedException;
import org.star.apigateway.web.model.jwt.TokensBundle;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public void register(
            final String login,
            final String email,
            final String password
    ) {
        if (userRepository.existsByEmailOrLogin(email, login)) {
            log.info("user exists with email {} or login {}", email, login);
            throw new EntityExistException(
                    "user with email " + email + " or login " + login + " already exist"
            );
        }
        final String hashPassword = encoder.encrypt(password);
        Role role = roleRepository.findByRole(RolesEnum.USER.toString()).orElseThrow(
                () -> new NotFoundException("role with provided name not found")
        );
        User user = new User(login, email, hashPassword);
        user.getRoles().add(role);
        log.info("save user {}", user.getLogin());
        userRepository.save(user);
    }

    public TokensBundle login(
            final String login,
            final String password
    ) {
        log.info("login user");
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> {
                    log.error("user not found");
                    return new NotFoundException("user with login: " + login + " not found");
                }
        );
        System.out.println(user.getRoles());
        if (!user.getEnabled()) {
            throw new UnauthorizedException("user with login: " + login + " ban");
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("pass not matches");
        }

        TokensBundle bundle = new TokensBundle();
        String accessToken = jwtService.createAccessToken(user);
        log.info("try bundle token access");
        bundle.setAccessToken(jwtService.createAccessToken(user));
        log.info("bundled token access");
        bundle.setRefreshToken(jwtService.upsertRefreshToken(user));

        return bundle;
    }

    public User findById(final String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
                    log.error("not found user {}", userId);
                    return new NotFoundException("User with id " + userId + " not found");
                }
        );
    }

    public User findIfPresent(final String userId) {
        User user = findById(userId);
        if (user.getEnabled()) {
            return user;
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
