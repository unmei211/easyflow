package org.star.socialservice.core.model.policies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PolicyBundle {
    private Boolean allowedToFriendRequest;
    private Boolean allowedSendTask;
}
