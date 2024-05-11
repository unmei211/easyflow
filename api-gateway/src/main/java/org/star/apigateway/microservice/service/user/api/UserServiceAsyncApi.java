package org.star.apigateway.microservice.service.user.api;

import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;
import reactor.core.publisher.Mono;

public interface UserServiceAsyncApi {
    Mono<UserViaId> saveUserAsync(UserToSaveTransfer userToSave);

    Mono<UserViaInfo> findUserByLoginAsync(String login);
}
