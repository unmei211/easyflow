package org.star.userservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.userservice.core.models.UserOfUserService;

import java.util.Optional;

public interface UserServiceRepository extends JpaRepository<UserOfUserService, String> {
    boolean existsByEmailOrLogin(String email, String login);

    boolean existsByEmail(String email);

    Boolean existsByLogin(String login);

    Optional<UserOfUserService> findUserByLogin(String login);
}
