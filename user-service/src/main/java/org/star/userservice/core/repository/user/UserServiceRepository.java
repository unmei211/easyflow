package org.star.userservice.core.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.userservice.core.models.user.UserUService;

import java.util.Optional;

public interface UserServiceRepository extends JpaRepository<UserUService, String> {
    boolean existsByEmailOrLogin(String email, String login);

    boolean existsByEmail(String email);

    Boolean existsByLogin(String login);

    Optional<UserUService> findUserByLogin(String login);
}
