package org.star.socialservice.web.model.token;


import lombok.Getter;

@Getter
public class AccessToken {
    private final String accessToken;

    public AccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
