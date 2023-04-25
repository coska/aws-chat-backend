package com.coska.aws.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class MyJwtAuthenticationToken extends JwtAuthenticationToken {
    private final String email;
    private final String name;
    private final String emailClaimValue = "email";
 
	public MyJwtAuthenticationToken(Jwt jwt) {
		super(jwt);
		setAuthenticated(false);
        this.name = jwt.getSubject();
        this.email = jwt.getClaimAsString(this.emailClaimValue);
	}

	public MyJwtAuthenticationToken(Jwt jwt,
			Collection<? extends GrantedAuthority> authorities) {
		super(jwt, authorities);
		super.setAuthenticated(true); 
        this.name = jwt.getSubject();
        this.email = jwt.getClaimAsString(this.emailClaimValue);
	}

    public MyJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities);
        super.setAuthenticated(true); 
        this.name = name;
        this.email = jwt.getClaimAsString(this.emailClaimValue);
    }

    public MyJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name, String email) {
        super(jwt, authorities);
        super.setAuthenticated(true); 
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
    
}
