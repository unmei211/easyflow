package org.star.easyflow.core.repository.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.easyflow.core.models.user.UserPolicy;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<UserPolicy, String> {
    Optional<UserPolicy> findByUserId(String userId);
}
