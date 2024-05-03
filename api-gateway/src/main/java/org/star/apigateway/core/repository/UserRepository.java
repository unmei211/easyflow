package org.star.apigateway.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.apigateway.core.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailOrLogin(String email, String login);
    boolean existsByEmail(String email);
    Boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);
}
