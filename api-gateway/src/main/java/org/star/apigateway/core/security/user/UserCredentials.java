package org.star.apigateway.core.security.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import org.star.apigateway.core.model.roles.Role;


import java.util.*;

/**
 * UserCredentials
 */
@Getter
public class UserCredentials {
    public static final String USER_CREDENTIALS = "userCredentialsAttr";
    private static final ObjectMapper mapper = new ObjectMapper();
    @JsonProperty("userId")
    private final String userId;

    @JsonProperty("roles")
    private final List<String> roles;

    @JsonCreator
    public UserCredentials(
            final String userId,
            final List<String> roles
    ) {
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            System.out.println("ex");
            return "";
        }
    }

    public static UserCredentials toPresent(final String json, final ObjectMapper mapper) {
        try {
            System.out.println(json);
            return mapper.readValue(json, UserCredentials.class);
        } catch (Exception e) {
            System.out.println("ex");
            return null;
        }
    }
}
