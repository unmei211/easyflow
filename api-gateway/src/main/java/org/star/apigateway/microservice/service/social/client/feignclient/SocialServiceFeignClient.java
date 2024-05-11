package org.star.apigateway.microservice.service.social.client.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.star.apigateway.microservice.service.social.api.SocialServiceApi;

@FeignClient(name = "socialClient")
public interface SocialServiceFeignClient extends SocialServiceApi {

}
