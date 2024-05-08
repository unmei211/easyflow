package org.star.userservice.web.controller.user;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
import org.star.userservice.core.models.user.UserUService;
import org.star.userservice.core.services.user.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserServiceController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserViaId> saveUser(@RequestBody UserToSaveTransfer userToSave) {
        System.out.println(userToSave.getEmail() + " bebebebe");
        return new ResponseEntity<>(new UserViaId(userService.saveUser(userToSave.getEmail(), userToSave.getLogin()).getId()), HttpStatus.CREATED);
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserViaInfo> findUserByLogin(@NotNull @PathVariable("login") String login) {
        UserUService userFromService = userService.findUserByLogin(login);
        UserViaInfo user = UserViaInfo.builder()
                .id(userFromService.getId())
                .login(userFromService.getLogin())
                .email(userFromService.getEmail())
                .build();
        return ResponseEntity.ok(user);
    }
}
