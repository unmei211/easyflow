package org.star.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.star.apigateway.microservice.service.social.client.feignclient.SocialServiceFeignClient;

@EnableDiscoveryClient
@EnableFeignClients(
        clients = {SocialServiceFeignClient.class}
)
@SpringBootApplication
class TaskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
