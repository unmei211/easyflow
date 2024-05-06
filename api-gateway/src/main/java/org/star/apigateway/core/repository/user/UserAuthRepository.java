package org.star.apigateway.core.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.star.apigateway.core.model.user.UserAuth;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
//    Optional<UserAuth> findBy
}
