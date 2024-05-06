package org.star.socialservice.core.services.friend;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.star.socialservice.core.models.user.User;
import org.star.socialservice.core.repository.user.UserRepository;
import org.star.socialservice.web.exception.core.NotFoundException;

@Service
@AllArgsConstructor
public class FriendService {
    private final UserRepository userRepository;


    public void friendRequest(final String userId, final String addresseeId) {
        User user = userRepository.findById(addresseeId).orElseThrow(
                () -> new NotFoundException("Not found user " + addresseeId)
        );

//        user.;
    }
}
