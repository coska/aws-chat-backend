package com.coska.aws.controller;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMyJwtAuthenticationTokenSecurityContextFactory.class)
public @interface WithMockMyJwtAuthenticationToken {
    String username() default "";

    String authority() default "";

    String email() default "test@test.com";

    boolean authenticated() default true;
}
