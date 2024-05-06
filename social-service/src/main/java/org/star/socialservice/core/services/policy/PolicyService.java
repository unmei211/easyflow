package org.star.socialservice.core.services.policy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.socialservice.core.models.user.UserPolicy;
import org.star.socialservice.core.repository.policy.PolicyRepository;
import org.star.socialservice.core.repository.user.UserRepository;
import org.star.socialservice.web.exception.core.ConflictException;
import org.star.socialservice.web.exception.core.NotFoundException;

@Service
@AllArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public UserPolicy findUserPolicy(final String userId) {
        return policyRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("Not found user policy")
        );
    }

    public boolean existUserPolicy(final String userId) {
        return policyRepository.existsByUserId(userId);
    }

    public void initUserPolicy(final String userId) {
        if (existUserPolicy(userId)) {
            throw new ConflictException("User policy already init");
        }

        policyRepository.save(new UserPolicy(userId));
    }

}
