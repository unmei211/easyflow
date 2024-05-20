package org.star.apigateway.microservice.service.social.client.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.star.apigateway.microservice.service.social.api.SocialServiceApi;
import org.star.apigateway.microservice.share.model.user.UserViaId;

@FeignClient(name = "social-service")
public interface SocialServiceFeignClient {
    @GetMapping("/social/{userId}/friend/{friendId}/is-friend")
    Boolean userIsFriend(@PathVariable(name = "userId") String userId, @PathVariable(name = "friendId") String userToCheck);
}
