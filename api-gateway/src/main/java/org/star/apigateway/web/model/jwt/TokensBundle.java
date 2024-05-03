package org.star.apigateway.web.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.apigateway.core.model.token.RefreshToken;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokensBundle {
    private String accessToken;
    private String refreshToken;

    public TokensBundle(
            final AccessToken accessToken,
            final RefreshToken refreshToken
    ) {
        this.accessToken = accessToken.getAccessToken();
        this.refreshToken = refreshToken.getToken();
    }
}
