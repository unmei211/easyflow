package org.star.socialservice.core.repository.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.socialservice.core.models.user.UserPolicy;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<UserPolicy, String> {
    Optional<UserPolicy> findByUserId(String userId);

    Boolean existsByUserId(String userId);
}
