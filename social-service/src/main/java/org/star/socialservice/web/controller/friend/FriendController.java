package org.star.socialservice.web.controller.friend;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.star.socialservice.core.services.friend.FriendService;

@RestController
@RequestMapping("/user/friend")
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/send")
    public ResponseEntity<?> friendRequest() {
//        friendService.
        return null;
    }
}
