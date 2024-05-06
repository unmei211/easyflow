package org.star.apigateway.microservice.social.api.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.star.apigateway.microservice.social.api.SocialServiceApi;

@FeignClient(name = "socialClient")
public interface SocialServiceFeignClient extends SocialServiceApi {

}
