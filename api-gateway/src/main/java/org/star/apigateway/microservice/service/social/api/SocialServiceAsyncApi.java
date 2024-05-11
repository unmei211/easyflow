package org.star.apigateway.microservice.service.social.api;

import org.star.apigateway.microservice.share.model.user.UserViaId;
import reactor.core.publisher.Mono;

public interface SocialServiceAsyncApi {
    Mono<Void> createSocial(UserViaId userViaId);
}
