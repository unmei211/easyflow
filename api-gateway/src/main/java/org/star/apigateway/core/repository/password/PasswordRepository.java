package org.star.apigateway.core.repository.password;

import org.springframework.data.repository.CrudRepository;
import org.star.apigateway.core.entity.password.Password;

import java.util.Optional;

public interface PasswordRepository extends CrudRepository<Password, String> {
    Optional<Password> findByUserId(String userId);
    Optional<Password> findByValue(String hashPassword);
}
