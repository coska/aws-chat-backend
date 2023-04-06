package com.coska.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CognitoSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] permitAllPatterns = {
            "/actuator/**",
            "/swagger-ui/**",
            "/coskachat-api-docs/**",
            "/coskachat-documentation",
            "/example/employees"
        };
        http.cors().and().csrf().disable()
        .authorizeHttpRequests(requests -> 
            requests.requestMatchers(permitAllPatterns).permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer().jwt();
        return http.build();
    }

}
