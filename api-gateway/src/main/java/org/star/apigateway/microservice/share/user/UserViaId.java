package org.star.apigateway.microservice.share.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViaId {
    private String userId;
}
