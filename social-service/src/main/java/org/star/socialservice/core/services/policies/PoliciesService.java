package org.star.socialservice.core.services.policies;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.socialservice.core.entity.policies.UserPolicies;
import org.star.socialservice.core.repository.policies.PoliciesRepository;

@Service
@AllArgsConstructor
@Slf4j
public class PoliciesService {
    private final PoliciesRepository policyRepository;

    public UserPolicies findUserPolicy(final String userId) {
        return policyRepository.findByUserId(userId).orElseThrow(
                () -> {
                    log.info("Can't find policies for {}", userId);
                    return new NotFoundException("Not found user policy");
                }
        );
    }

    public boolean existUserPolicy(final String userId) {
        return policyRepository.existsByUserId(userId);
    }

    public void initUserPolicy(final String userId) {
        if (existUserPolicy(userId)) {
            throw new ConflictException("User policy already init");
        }

        policyRepository.save(new UserPolicies(userId));
    }

    public boolean availableForFriendRequests(final String userId) {
        return findUserPolicy(userId).getAllowedToFriendRequest();
    }

    public boolean availableForSendTask(final String userId) {
        return findUserPolicy(userId).getAllowedSendTask();
    }
}
