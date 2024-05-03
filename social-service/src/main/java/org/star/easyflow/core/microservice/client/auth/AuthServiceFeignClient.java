package org.star.easyflow.core.microservice.client.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.star.easyflow.core.microservice.client.interceptor.MicroserviceAuth;

@FeignClient(name = "authClient")
public interface AuthServiceFeignClient {
    @GetMapping("/user/present")
    @MicroserviceAuth
    void userIsPresent();
}
