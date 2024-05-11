package org.star.apigateway.core.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.apigateway.core.entity.user.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
//    Optional<UserAuth> findBy
}
