package org.star.socialservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HealthCheck {
    @GetMapping("/user/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Heath check");
        return ResponseEntity.ok("health");
    }
}
