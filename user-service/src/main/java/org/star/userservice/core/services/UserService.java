package org.star.userservice.core.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.userservice.core.models.UserOfUserService;
import org.star.userservice.core.repository.UserServiceRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserServiceRepository serviceRepository;

    public Boolean userIdIsExist(final String userId) {
        return serviceRepository.existsById(userId);
    }

    public UserOfUserService findUserById(final UserViaId user) {
        return serviceRepository.findById(user.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    public Boolean existsByEmailOrLogin(final String email, final String login) {
        return serviceRepository.existsByEmailOrLogin(email, login);
    }

    public UserOfUserService saveUser(final String email, final String login) {
        if (existsByEmailOrLogin(email, login)) {
            throw new ForbiddenException("User with provided email and login exist");
        }
        return serviceRepository.save(new UserOfUserService(login, email));
    }

    public UserOfUserService findUserByLogin(final String login) {
        return serviceRepository.findUserByLogin(login).orElseThrow(
                () -> new NotFoundException("User via login not found")
        );
    }
}
