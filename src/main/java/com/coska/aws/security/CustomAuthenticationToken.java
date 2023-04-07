package com.coska.aws.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CustomAuthenticationToken extends JwtAuthenticationToken {
	public CustomAuthenticationToken(Jwt jwt) {
		super(jwt);
		setAuthenticated(false);
	}

	public CustomAuthenticationToken(Jwt jwt,
			Collection<? extends GrantedAuthority> authorities) {
		super(jwt, authorities);
		super.setAuthenticated(true); 
	}

    public String getEmail() {
        return this.getToken().getClaimAsString("email");
    }

    public String getId() {
        return this.getToken().getClaimAsString("sub");
    }
    
}
