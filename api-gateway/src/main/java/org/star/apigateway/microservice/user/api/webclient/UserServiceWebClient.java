package org.star.apigateway.microservice.user.api.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.star.apigateway.microservice.share.user.UserViaId;
import org.star.apigateway.microservice.user.api.model.user.UserToSaveTransfer;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class UserServiceWebClient {
    private @Value("${easyflow.ecosystem.user-service.url}") String baseUrl;
    private final WebClient client;

    public UserServiceWebClient() {
        this.client = WebClient.builder()
                .baseUrl("http://127.0.0.1:8105")
                .build();
    }

    public Mono<UserViaId> saveUserAsync(final UserToSaveTransfer userToSave) {
        return client
                .post()
                .uri(String.join("", "/user","/save"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToSave)
                .retrieve()
                .onStatus(HttpStatus::is)
                .bodyToMono(UserViaId.class);
    }
}
