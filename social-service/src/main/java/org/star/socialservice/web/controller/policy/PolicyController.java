package org.star.socialservice.web.controller.policy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.socialservice.core.microservice.client.auth.AuthServiceFeignClient;
import org.star.socialservice.core.models.user.UserPolicy;
import org.star.socialservice.core.services.policy.PolicyService;
import org.star.socialservice.web.exception.core.NotFoundException;
import org.star.socialservice.web.model.user.UserCredentials;
import org.star.socialservice.web.model.user.UserToInitial;

@RestController
@RequestMapping("/user/policy")
@AllArgsConstructor
@Slf4j
public class PolicyController {
    private final PolicyService policyService;
    private final AuthServiceFeignClient authClient;

    @GetMapping
    public ResponseEntity<?> getUserPolicy(
            UserCredentials credentials
    ) {
        try {
            authClient.userIsPresent();
        } catch (Exception e) {
            throw new NotFoundException("User not found or not present");
        }
        UserPolicy userPolicy = policyService.findUserPolicy(credentials.getUserId());

        return new ResponseEntity<>(userPolicy.getPolicyBundle(), HttpStatus.OK);
    }

    @PostMapping
    // ServiceAuthorized
    public ResponseEntity<?> initAfterCreate(
            @RequestBody UserToInitial user
    ) {
        policyService.initUserPolicy(user.getUserId());
        return null;
    }
}
