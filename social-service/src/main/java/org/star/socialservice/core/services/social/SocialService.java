package org.star.socialservice.core.services.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.socialservice.core.entity.policies.UserPolicies;
import org.star.socialservice.core.entity.user.SocialUser;
import org.star.socialservice.core.repository.policies.PoliciesRepository;
import org.star.socialservice.core.repository.social.SocialRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialService {
    private final SocialRepository socialRepository;
    private final PoliciesRepository policiesRepository;

    public void createSocial(String userId) {
        if (socialRepository.existsById(userId)) {
            throw new ForbiddenException("User is exist");
        }

        SocialUser user = new SocialUser(userId);
        UserPolicies policies = new UserPolicies(userId);
        user.setPolicy(
                policies
        );
        socialRepository.save(user);
        policiesRepository.save(policies);
    }

    public SocialUser findUserViaId(String userId) {
        return socialRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }


    public void sendFriendRequest(String userIdFrom, String userIdTo) {
        System.out.println(userIdFrom);
        System.out.println(userIdTo);
        SocialUser from = findUserViaId(userIdFrom);
        SocialUser to = findUserViaId(userIdTo);
        log.info("Find user from {}", from);
        log.info("Find user to {}", to);


        if (from.getFriends().contains(to)) {
            log.info("User {} already friend to {}", from.getId(), to.getId());
            throw new ConflictException("User is friend");
        }

        if (to.getFriendRequests().contains(from)) {
            log.info("User {} already send request to {}", from.getId(), to.getId());
            throw new ConflictException("Already invite");
        }

        if (from.getFriendRequests().contains(to)) {
            from.removeFriendRequest(to);
            acceptFriendRequest(from, to);
            return;
        }

        to.addFriendRequest(from);
        socialRepository.save(to);
    }

    public void acceptFriendRequest(SocialUser from, SocialUser to) {
        from.addFriend(to);
        socialRepository.save(from);
        socialRepository.save(to);
    }
}
