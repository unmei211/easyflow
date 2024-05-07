package org.star.apigateway.microservice.share.error.handlers.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.star.apigateway.microservice.share.error.handlers.ErrorsAssociate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebClientErrorHandlerFilter implements ExchangeFilterFunction {
//    private final ErrorsAssociate associate;
//
//    @NonNull
//    @Override
//    public Mono<ClientResponse> filter(
//            final @NonNull ClientRequest request,
//            final @NonNull ExchangeFunction next
//    ) {
//        return next.exchange(request)
//                .flatMap(response -> {
//                    HttpStatusCode status = response.statusCode();
//                    if (status.is4xxClientError() || status.is5xxServerError()) {
//                        associate.ifStatus(status.value());
//                    }
//                    return Mono.just(response);
//                });
//    }
}
