package org.star.apigateway.microservice.service.task.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "task-service")
public interface TaskServiceFeignClient {
    @GetMapping("/task")
    String getCheck();
}
