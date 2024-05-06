package org.star.apigateway.microservice.user.api.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.star.apigateway.microservice.share.user.UserViaId;
//import org.star.apigateway.microservice.user.api.UserServiceApi;
import org.star.apigateway.microservice.user.api.model.user.UserToSaveTransfer;
//import org.star.apigateway.microservice.user.api.model.user.UserUserServiceTransfer;


@FeignClient(name = "userClient")
public interface UserServiceFeignClient {
    @PostMapping("/user/save")
//    @Override
    UserViaId saveUser(@RequestBody UserToSaveTransfer userToSave);

    //    @Override
    @GetMapping("/user/{login}")
    UserViaId findUserByLogin(@PathVariable(name = "login") String login);

    @GetMapping("/user/beba")
    void beba();
}
