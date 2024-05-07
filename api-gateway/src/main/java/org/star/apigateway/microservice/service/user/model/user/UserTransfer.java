package org.star.apigateway.microservice.service.user.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTransfer {
    private String id;
    private String login;
    private String email;
    private Boolean enabled;
}
