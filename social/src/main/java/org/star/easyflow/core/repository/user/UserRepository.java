package org.star.easyflow.core.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.easyflow.core.models.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailOrLogin(String email, String login);
    boolean existsByEmail(String email);
    Boolean existsByLogin(String login);
}
