package org.star.socialservice.core.repository.policies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.socialservice.core.entity.policies.UserPolicies;

import java.util.Optional;

public interface PoliciesRepository extends JpaRepository<UserPolicies, String> {
    Optional<UserPolicies> findByUserId(String userId);

    Boolean existsByUserId(String userId);
}
