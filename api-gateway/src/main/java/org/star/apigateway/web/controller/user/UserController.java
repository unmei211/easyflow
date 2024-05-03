package org.star.apigateway.web.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.core.security.resolver.AuthRoleRequired;
import org.star.apigateway.core.security.user.UserCredentials;
import org.star.apigateway.core.service.auth.AuthService;
import org.star.apigateway.web.model.user.request.UserRequestBody;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    AuthService service;

    @GetMapping("/present")
    @AuthRoleRequired(value = "USER")
    public ResponseEntity<?> userIsPresent(
            @RequestHeader(name = "ServiceAuthorization") String serviceAuth
//            final UserCredentials userCredentials,
//            final @RequestBody UserRequestBody body
            ) {
        System.out.println("check");
        System.out.println(serviceAuth);
//        service.findIfPresent(body.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
