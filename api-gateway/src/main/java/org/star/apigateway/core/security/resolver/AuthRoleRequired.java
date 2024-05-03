package org.star.apigateway.core.security.resolver;

import java.lang.annotation.*;

/**
 * Annotation on controller methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthRoleRequired {
    /**
     * required role
     *
     * @return role
     */
    String value() default "";

    String[] required () default {};

    boolean anyRole() default false;
}
