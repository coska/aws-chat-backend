package com.coska.aws.controller;

import com.coska.aws.security.AuthCurrentUser;
import com.coska.aws.security.MyJwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/example/security")
public class SecurityTestController {

    @GetMapping
    public String getSecurityTest(
        @AuthCurrentUser MyJwtAuthenticationToken authUser
    ) {
        return authUser.getEmail();
    }
    
}
