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
import org.star.socialservice.core.model.policies.PolicyBundle;
import org.star.socialservice.core.services.policies.PoliciesService;

@RestController
@RequestMapping("/social/policy")
@AllArgsConstructor
@Slf4j
public class PolicyController {
    private final PoliciesService policyService;

    @GetMapping
    public ResponseEntity<?> getUserPolicy(
            UserCredentials credentials,
            final @RequestBody UserViaId userViaId
    ) {
        UserPolicies userPolicy = policyService.findUserPolicy(userViaId.getUserId());

        return new ResponseEntity<>(userPolicy.getPolicyBundle(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> changePolicy(
            UserCredentials userCredentials,
            final @RequestBody PolicyBundle policiesBundle
    ) {
        policyService.changePolicy(userCredentials.getUserId(), policiesBundle);
        return ResponseEntity.ok().build();
    }
}
