package org.star.apigateway.microservice.share.transfer.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * UserCredentials
 */
@Getter
@Setter
@AllArgsConstructor
public class UserCredentials {
    @JsonIgnore
    public static final String USER_CREDENTIALS = "userCredentialsAttr";
    private String userId;

    private List<String> roles;

    public static Optional<UserCredentials> parse(final String json, final ObjectMapper mapper) {
        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }
        try {
            System.out.println(json);
            return Optional.of(mapper.readValue(json, UserCredentials.class));
        } catch (Exception e) {
            System.out.println("ex");
            return Optional.empty();
        }
    }

    public static String stringify(final UserCredentials userCredentials, final ObjectMapper mapper) {
        if (userCredentials == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(userCredentials);
        } catch (Exception e) {
            return null;
        }
    }

}