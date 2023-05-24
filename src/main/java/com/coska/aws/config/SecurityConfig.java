package com.coska.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.coska.aws.security.MyJwtAuthenticationConverter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ROLES_CLAIM_NAME="cognito:groups";
    private static final String ROLE_PREFIX = "";

    @Bean
    MyJwtAuthenticationConverter jwtAuthenticationConverter(){
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(ROLES_CLAIM_NAME);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);
        var myJwtAuthenticationConverter =  new MyJwtAuthenticationConverter();
        myJwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return myJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] permitAllPatterns = {
            "/actuator/**",
            "/swagger-ui/**",
            "/chat-coska-com-api-docs/**",
            "/chat-coska-com-api-documentation",
            "/example/employees/**",
            "/ping",
            "/v1/**",
            "/message/**",
            "/aws-chat/**",
            "/publish/**"
        };
        http.cors().and().csrf().disable()
        .authorizeHttpRequests(requests -> 
            requests.requestMatchers(permitAllPatterns).permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer().jwt()
        .jwtAuthenticationConverter(jwtAuthenticationConverter());

        return http.build();
    }

}
