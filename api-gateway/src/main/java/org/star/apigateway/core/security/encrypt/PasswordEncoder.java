package org.star.apigateway.core.security.encrypt;

/**
 * PasswordEncoder
 */
public interface PasswordEncoder {
    /**
     * matches
     * @param basePassword basePass
     * @param hashedPassword hashed
     * @return matches
     */
    boolean matches(String basePassword, String hashedPassword);

    /**
     * encrypt password
     * @param basePassword basePassword
     * @return hashedPassword
     */
    String encrypt(String basePassword);
}
