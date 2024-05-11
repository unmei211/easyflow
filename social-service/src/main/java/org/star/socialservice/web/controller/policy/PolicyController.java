package org.star.socialservice.web.controller.policy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.service.auth.api.feignclient.AuthServiceFeignClient;
import org.star.apigateway.microservice.share.error.exceptions.core.NotFoundException;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.transfer.user.UserCredentials;
import org.star.socialservice.core.entity.policies.UserPolicies;
import org.star.socialservice.core.services.policies.PoliciesService;

@RestController
@RequestMapping("/user/policy")
@AllArgsConstructor
@Slf4j
public class PolicyController {
    private final PoliciesService policyService;
    private final AuthServiceFeignClient authClient;

    @GetMapping
    public ResponseEntity<?> getUserPolicy(
            UserCredentials credentials
    ) {
        try {
//            authClient.userIsPresent();
        } catch (Exception e) {
            throw new NotFoundException("User not found or not present");
        }
        UserPolicies userPolicy = policyService.findUserPolicy(credentials.getUserId());

        return new ResponseEntity<>(userPolicy.getPolicyBundle(), HttpStatus.OK);
    }

    @PostMapping
    // ServiceAuthorized
    public ResponseEntity<?> initAfterCreate(
            @RequestBody UserViaId user
    ) {
        policyService.initUserPolicy(user.getUserId());
        return null;
    }
}
