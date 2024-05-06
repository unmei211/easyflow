package org.star.socialservice.web.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
