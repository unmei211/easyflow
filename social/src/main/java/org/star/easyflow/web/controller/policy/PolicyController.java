package org.star.easyflow.web.controller.policy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.star.easyflow.core.microservice.client.auth.AuthServiceFeignClient;
import org.star.easyflow.core.repository.user.UserRepository;
import org.star.easyflow.core.services.policy.PolicyService;
import org.star.easyflow.web.exception.core.NotFoundException;
import org.star.easyflow.web.model.token.AccessToken;
import org.star.easyflow.web.model.user.UserCredentials;

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
        return ResponseEntity.ok(policyService.findUserPolicy(credentials.getUserId()));
    }
}
