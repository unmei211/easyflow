package org.star.apigateway.microservice.service.user.api;

import org.star.apigateway.microservice.service.user.model.user.UserToSaveTransfer;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import org.star.apigateway.microservice.share.model.user.UserViaInfo;

public interface UserServiceApi {
    UserViaId createUser(UserToSaveTransfer userToSave);

    UserViaInfo findUserByLogin(String login);
}
