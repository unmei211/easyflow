package org.star.socialservice.core.services.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
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

    public Boolean isFriend(String userId, String userIdToCheck) {
        SocialUser user = socialRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Can't find requester")
        );
        SocialUser toCheck = socialRepository.findById(userIdToCheck).orElseThrow(
                () -> new NotFoundException("Can't find user to check")
        );
        return user.getFriends().contains(toCheck);
    }


    public void sendFriendRequest(String userIdFrom, String userIdTo) {
        SocialUser from = findUserViaId(userIdFrom);
        SocialUser to = findUserViaId(userIdTo);
        log.info("Find user from {}", from);
        log.info("Find user to {}", to);


        if (from.isFriend(to)) {
            log.info("User {} already friend to {}", from.getId(), to.getId());
            throw new ConflictException("User is friend");
        }

        if (to.getFriendRequests().contains(from)) {
            log.info("User {} already send request to {}", from.getId(), to.getId());
            throw new ConflictException("Already invite");
        }

        if (from.getFriendRequests().contains(to)) {
            from.removeFriendRequest(to);
            bindFriends(from, to);
            return;
        }

        to.addFriendRequest(from);
        socialRepository.save(to);
    }

    private void bindFriends(SocialUser first, SocialUser second) {
        first.addFriend(second);
        socialRepository.save(first);
        socialRepository.save(second);
    }

    public void acceptFriendRequest(SocialUser sender, SocialUser receiver) {
        if (receiver.isFriend(sender)) {
            throw new ConflictException("Already friend");
        }

        if (receiver.haveInviteFrom(sender)) {
            receiver.removeFriendRequest(sender);
            bindFriends(sender, receiver);
        }
    }
}
