package com.coska.aws.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public final Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
}
