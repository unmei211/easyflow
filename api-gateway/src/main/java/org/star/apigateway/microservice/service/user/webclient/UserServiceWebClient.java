package org.star.apigateway.microservice.service.user.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.star.apigateway.microservice.service.user.UserServiceApi;
import org.star.apigateway.microservice.service.user.UserServiceAsyncApi;
import org.star.apigateway.microservice.share.error.exceptions.security.ForbiddenException;
import org.star.apigateway.microservice.share.error.exceptions.security.UnauthorizedException;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Slf4j
@Component
public class UserServiceWebClient {
    private final WebClient client;
    private final ErrorsAssociate associate;

    public UserServiceWebClient(
            final @Value("${easyflow.ecosystem.user-service.url}") String baseUrl,
            final ErrorsAssociate associate
    ) {
        this.associate = associate;
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }

    private static void logPutError(final ClientResponse clientResponse) {
        log.info("Throw for reactive chain status: {}\tfrom request: saveUserAsync", clientResponse.statusCode().value());
    }

    public Mono<UserViaId> saveUser(final UserToSaveTransfer userToSave) {
        return client
                .post()
                .uri(String.join("", "/user", "/create"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToSave)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (response) -> Mono.error(() -> {
                            logPutError(response);
                            return associate.getInit(response.statusCode());
                        })
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                () -> {
                                    logPutError(response);
                                    return associate.getInit(response.statusCode());
                                }
                        ))
                .bodyToMono(UserViaId.class);
    }

    public Mono<UserViaInfo> findUserByLogin(String login) {
        return client
                .get()
                .uri(String.join("", "/user", "/", login))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse ->
                                Mono.error(() -> {
                                    logPutError(clientResponse);
                                    return associate.getInit(clientResponse.statusCode());
                                }))
                .bodyToMono(UserViaInfo.class);
    }
}
