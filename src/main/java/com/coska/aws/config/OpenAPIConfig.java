package com.coska.aws.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
        contact.setEmail("admin@coska.chat.com");
        contact.setName("CoskaChat");
        contact.setUrl("https://www.coskachat.com");

        License mitLicense = new License().name("MIT License").url("https://www.coskachat.com/licenses/mit/");

        Info info = new Info()
                .title("Coska Chat API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage Coska Chat.").termsOfService("https://www.coskachat.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
