package org.star.easyflow.web.model.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class AccessToken {
    private final String accessToken;

    public AccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
