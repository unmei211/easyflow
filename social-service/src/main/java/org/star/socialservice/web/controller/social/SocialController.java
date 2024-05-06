package org.star.socialservice.web.controller.social;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.star.socialservice.core.services.policy.PolicyService;
import org.star.socialservice.core.services.social.SocialService;

@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;
    private final PolicyService policyService;
//    @PostMapping("/user")
//    //ServiceAuthorizationRequired
//    public ResponseEntity<?> initAfterCreate(
//            @RequestBody UserToInitial user
//    ) {
//
//    }
}
