package org.star.apigateway.microservice.service.social.client.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.star.apigateway.microservice.service.social.api.SocialServiceAsyncApi;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SocialServiceWebClient implements SocialServiceAsyncApi {
    private final WebClient client;
    private final ErrorsAssociate associate;

    public SocialServiceWebClient(
            final @Value("${easyflow.ecosystem.social-service.url}") String baseUrl,
            final ErrorsAssociate associate
    ) {
        this.associate = associate;
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }

    private static void logPutError(final ClientResponse clientResponse) {
        log.info("Throw for reactive chain status: {}\tfrom request: saveUserAsync", clientResponse.statusCode().value());
    }

    @Override
    public Mono<Void> createSocial(final UserViaId userViaId) {
        return client
                .post()
                .uri("/social/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userViaId)
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
                .bodyToMono(Void.class);
    }
}
