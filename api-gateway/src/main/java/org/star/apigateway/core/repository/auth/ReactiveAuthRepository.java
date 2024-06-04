package org.star.apigateway.core.repository.auth;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.star.apigateway.core.entity.user.UserAuth;
import reactor.core.publisher.Mono;

public interface ReactiveAuthRepository extends ReactiveCrudRepository<UserAuth, String> {

}
