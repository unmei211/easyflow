package org.star.socialservice.core.microservice.client.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authClient")
public interface AuthServiceFeignClient {
    @GetMapping("/user/present")
    void userIsPresent();
}
