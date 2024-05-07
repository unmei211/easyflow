package org.star.apigateway.microservice.service.user;

import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaId;

public interface UserServiceApi {
    UserViaId saveUser(UserToSaveTransfer userToSave);

    UserViaId findUserByLogin(String login);
}
