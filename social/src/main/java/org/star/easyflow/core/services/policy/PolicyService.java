package org.star.easyflow.core.services.policy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.easyflow.core.models.policy.PolicyBundle;
import org.star.easyflow.core.models.user.User;
import org.star.easyflow.core.models.user.UserPolicy;
import org.star.easyflow.core.repository.policy.PolicyRepository;
import org.star.easyflow.core.repository.user.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public PolicyBundle findUserPolicy(final String userId) {
        Optional<UserPolicy> policy = policyRepository.findByUserId(userId);

        if (policy.isEmpty()) {
            System.out.println("wtf");
            return policyRepository.save(new UserPolicy(userId)).getPolicyBundle();
        }

        return policy.get().getPolicyBundle();
    }

}
