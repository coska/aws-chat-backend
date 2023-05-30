package com.coska.aws.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${coskachat.openapi.dev-url}")
    private String devUrl;

    @Value("${coskachat.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("coska21@coska.com");
        contact.setName("Chat Coska");
        contact.setUrl("https://chat.coska.com");

        License mitLicense = new License().name("MIT License").url("https://chat.coska.com/licenses/mit/");

        Info info = new Info()
                .title("Coska Chat API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage Coska Chat.").termsOfService("https://www.coskachat.com/terms")
                .license(mitLicense);

        SecurityScheme securityScheme = new SecurityScheme()
                .name("jwt")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return new OpenAPI()
        .info(info)
        .addSecurityItem(new SecurityRequirement().addList("jwt"))
        .components(new Components().addSecuritySchemes("jwt", securityScheme))
        .servers(List.of(devServer, prodServer));
    }
}
