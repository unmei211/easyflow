package org.star.apigateway.microservice.service.social.api;

import org.star.apigateway.microservice.share.model.user.UserViaId;

public interface SocialServiceApi {
    boolean userIsFriend(UserViaId user, String userToCheck);
}
