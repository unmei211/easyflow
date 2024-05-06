package org.star.socialservice.core.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.socialservice.core.models.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailOrLogin(String email, String login);
    boolean existsByEmail(String email);
    Boolean existsByLogin(String login);
}
