package org.star.apigateway.core.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.apigateway.core.model.roles.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(String role);
}
