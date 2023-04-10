package com.coska.aws.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
// @WebMvcTest(controllers = UserController.class)
public class SecurityTestControllerTest {
    @Autowired
    MockMvc mvc;
    @Test
    @WithMockMyJwtAuthenticationToken
    void testGetSecurityTest() throws Exception {
        mvc.perform(get("/example/security"))
        .andExpect(status().isOk())
        .andExpect(content().string("test@test.com"));
    }
}
