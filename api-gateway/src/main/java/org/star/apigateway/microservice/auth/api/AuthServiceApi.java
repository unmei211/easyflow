package org.star.apigateway.microservice.auth.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.star.apigateway.web.model.user.request.UserRequestBody;

public interface AuthServiceApi {
    void userIsPresent(UserRequestBody user);
}
