package org.star.apigateway.microservice.user.api.model.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserToSaveTransfer {
    private String login;
    private String email;
}
