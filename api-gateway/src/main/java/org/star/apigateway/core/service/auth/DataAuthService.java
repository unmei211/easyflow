package org.star.apigateway.core.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.core.model.user.UserAuth;
import org.star.apigateway.core.repository.user.UserAuthRepository;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataAuthService {
    private final UserAuthRepository userRepository;

    public UserAuth findById(final String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
                    log.error("not found user {}", userId);
                    return new NotFoundException("User with id " + userId + " not found");
                }
        );
    }

    public UserAuth findIfPresent(final String userId) {
        UserAuth user = findById(userId);
        if (user.getEnabled()) {
            return user;
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public List<UserAuth> findAllUsers() {
        return userRepository.findAll();
    }
}
