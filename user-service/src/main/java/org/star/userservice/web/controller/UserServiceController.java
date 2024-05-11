package org.star.userservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
import org.star.userservice.core.models.UserOfUserService;
import org.star.userservice.core.services.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "UserServiceController")
public class UserServiceController {
    private final UserService userService;

    @Operation(summary = "User registration",
            description = "Try to register")
    @PostMapping("/create")
    public ResponseEntity<UserViaId> createUser(@RequestBody UserToSaveTransfer userToSave) {
        UserOfUserService serviceUser = userService.saveUser(userToSave.getEmail(), userToSave.getLogin());
        UserViaId newUser = new UserViaId(serviceUser.getId());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserViaInfo> findUserByLogin(@NotNull @PathVariable("login") String login) {
        log.info("Try find user by login {}", login);
        UserOfUserService userFromService = userService.findUserByLogin(login);
        log.info("Find user {}", userFromService.toString());
        UserViaInfo user = UserViaInfo.builder()
                .id(userFromService.getId())
                .login(userFromService.getLogin())
                .email(userFromService.getEmail())
                .build();
        return ResponseEntity.ok(user);
    }
}
