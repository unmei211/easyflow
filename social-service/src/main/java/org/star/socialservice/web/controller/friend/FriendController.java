package org.star.socialservice.web.controller.friend;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.share.error.exceptions.core.ConflictException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.socialservice.core.entity.policies.UserPolicies;
import org.star.socialservice.core.entity.user.SocialUser;
import org.star.socialservice.core.services.friend.FriendService;
import org.star.socialservice.core.services.policies.PoliciesService;
import org.star.socialservice.core.services.social.SocialService;

@RestController
@RequestMapping("/social")
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final SocialService socialService;
    private final PoliciesService policiesService;

    @PostMapping("/friend/request")
    public ResponseEntity<?> friendRequest(
            final UserCredentials userCredentials,
            final @NotNull @RequestBody UserViaId userTo
    ) {
        if (policiesService.availableForFriendRequests(userTo.getUserId())) {
            socialService.sendFriendRequest(userCredentials.getUserId(), userTo.getUserId());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend/accept")
    public ResponseEntity<Void> friendAccept(
            final UserCredentials userCredentials,
            final @NotNull @RequestBody UserViaId senderBody
    ) {
        if (userCredentials.getUserId().equals(senderBody.getUserId())) {
            throw new ConflictException("Can't add self to friend");
        }
        SocialUser sender = socialService.findUserViaId(senderBody.getUserId());
        SocialUser receiver = socialService.findUserViaId(userCredentials.getUserId());

        socialService.acceptFriendRequest(sender, receiver);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/friend/{friendId}/is-friend")
    public ResponseEntity<Boolean> userIsFriend(
            @PathVariable(name = "userId") String userId,
            @PathVariable("friendId") String userIdToCheck
    ) {
        return ResponseEntity.ok(socialService.isFriend(userId, userIdToCheck));
    }
}
