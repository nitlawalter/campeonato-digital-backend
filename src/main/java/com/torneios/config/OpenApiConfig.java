package com.torneios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Campeonato Digital API")
                .description("API para gerenciamento de campeonatos de futebol")
                .version("1.0")
                .contact(new Contact()
                    .name("Time de Desenvolvimento")
                    .email("dev@campeonatodigital.com")))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
            .addServersItem(new Server()
                .url("/")
                .description("API Server"))
            .addServersItem(new Server()
                .url("/ws")
                .description("WebSocket Server"));
    }
} 