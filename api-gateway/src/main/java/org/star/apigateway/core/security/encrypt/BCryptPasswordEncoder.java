package org.star.apigateway.core.security.encrypt;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * BCryptEncoder
 */
@Component
public class BCryptPasswordEncoder implements PasswordEncoder {
    @Override
    public boolean matches(final String basePassword, final String hashedPassword) {
        return BCrypt.checkpw(basePassword, hashedPassword);
    }

    @Override
    public String encrypt(final String basePassword) {
        return BCrypt.hashpw(basePassword, BCrypt.gensalt());
    }
}
