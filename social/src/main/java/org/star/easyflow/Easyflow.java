package org.star.easyflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
class Easyflow {
    public static void main(String[] args) {
        SpringApplication.run(Easyflow.class, args);
    }
}
