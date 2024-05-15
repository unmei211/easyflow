package org.star.socialservice.core.services.friend;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.socialservice.core.entity.user.SocialUser;

@Service
@AllArgsConstructor
public class FriendService {


    public void friendRequest(final String userId, final String addresseeId) {
//        SocialUser user = userRepository.findById(addresseeId).orElseThrow(
//                () -> new NotFoundException("Not found user " + addresseeId)
//        );

//        user.;
    }
}
