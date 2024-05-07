package org.star.apigateway.microservice.service.auth.api.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.star.apigateway.microservice.service.auth.api.AuthServiceApi;
import org.star.apigateway.web.model.user.request.UserRequestBody;

@FeignClient(name = "authClient")
public interface AuthServiceFeignClient extends AuthServiceApi {
    @GetMapping("/auth/present")
    @Override
    void userIsPresent(@RequestBody UserRequestBody user);
}
