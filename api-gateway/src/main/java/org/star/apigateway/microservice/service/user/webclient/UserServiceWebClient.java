package org.star.apigateway.microservice.service.user.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Component
public class UserServiceWebClient {
    private final String baseUrl;
    private final WebClient client;
    private final ErrorsAssociate associate;

    public UserServiceWebClient(
            final @Value("${easyflow.ecosystem.user-service.url}") String baseUrl,
            final ErrorsAssociate associate
    ){
        this.baseUrl = baseUrl;
        this.associate = associate;
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }


    public Mono<UserViaId> saveUserAsync(final UserToSaveTransfer userToSave) {
        return client
                .post()
                .uri(String.join("", "/user", "/save"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToSave)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (response) -> {
                            System.out.println("bebab");
                            return Mono.error(new ForbiddenException("g"));
                        })
//                        response -> Mono.error(
//                                () -> associate.getInit(response.statusCode())
//                        ))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                () -> associate.getInit(response.statusCode())
                        ))
                .bodyToMono(UserViaId.class);
    }
}
