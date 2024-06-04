package org.star.apigateway.microservice.share.model.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViaInfo {
    private String id;
    private String login;
    private String email;
}
