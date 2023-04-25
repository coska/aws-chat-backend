package com.coska.aws.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.coska.aws.security.MyJwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WithMockMyJwtAuthenticationTokenSecurityContextFactory
        implements WithSecurityContextFactory<WithMockMyJwtAuthenticationToken> {

    @Override
    public SecurityContext createSecurityContext(WithMockMyJwtAuthenticationToken withMockMyJwtAuthenticationToken) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", withMockMyJwtAuthenticationToken.username());
        attributes.put("email", withMockMyJwtAuthenticationToken.email());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority(withMockMyJwtAuthenticationToken.authority()));
        var jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", withMockMyJwtAuthenticationToken.username())
        .claim("email", withMockMyJwtAuthenticationToken.email()).build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        MyJwtAuthenticationToken myJwtAuthenticationToken = new MyJwtAuthenticationToken(jwt, authorities, withMockMyJwtAuthenticationToken.username());
        context.setAuthentication(myJwtAuthenticationToken);
        return context;
    }
}