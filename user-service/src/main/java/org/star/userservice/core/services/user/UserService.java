package org.star.userservice.core.services.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.user.UserViaId;
import org.star.socialservice.web.exception.core.NotFoundException;
import org.star.userservice.core.models.user.UserUService;
import org.star.userservice.core.repository.user.UserServiceRepository;
import org.star.userservice.web.exception.security.ForbiddenException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserServiceRepository serviceRepository;

    public Boolean userIdIsExist(final String userId) {
        return serviceRepository.existsById(userId);
    }

    public UserUService findUserById(final UserViaId user) {
        return serviceRepository.findById(user.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    public Boolean existsByEmailOrLogin(final String email, final String login) {
        return serviceRepository.existsByEmailOrLogin(email, login);
    }

    public UserUService saveUser(final String email, final String login) {
        if (existsByEmailOrLogin(email, login)) {
            throw new ForbiddenException("User with provided email and login exist");
        }
        return serviceRepository.save(new UserUService(login, email));
    }

    public UserUService findUserByLogin(final String login) {
        return serviceRepository.findUserByLogin(login).orElseThrow(
                () -> new NotFoundException("User via login not found")
        );
    }
}
