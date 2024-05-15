package org.star.socialservice.web.controller.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.socialservice.core.services.policies.PoliciesService;
import org.star.socialservice.core.services.social.SocialService;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
@Slf4j
public class SocialController {
    private final SocialService socialService;
    private final PoliciesService policiesService;

    @PostMapping("/create")
    public ResponseEntity<Void> createSocial(@RequestBody UserViaId userViaId) {
        System.out.println(userViaId.getUserId());
        socialService.createSocial(userViaId.getUserId());
        return ResponseEntity.ok().build();
    }

}
