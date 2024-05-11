package org.star.apigateway.microservice.service.user.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.star.apigateway.microservice.service.user.UserServiceApi;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
//import org.star.apigateway.microservice.user.api.UserServiceApi;

//import org.star.apigateway.microservice.user.api.model.user.UserUserServiceTransfer;


@FeignClient(name = "userClient")
public interface UserServiceFeignClient extends UserServiceApi {
    @PostMapping("/user/create")
    UserViaId createUser(@RequestBody UserToSaveTransfer userToSave);

    @GetMapping("/user/{login}")
    UserViaInfo findUserByLogin(@PathVariable(name = "login") String login);
}
