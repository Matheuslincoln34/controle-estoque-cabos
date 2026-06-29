package com.seuportifolio.controle_cabos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Controle Logístico de Cabos")
                        .version("1.0")
                        .description("Sistema de auditoria e controle de estoque móvel para equipes de rua."))
                // Adiciona a exigência de segurança globalmente nas rotas
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // Define como é o botão do cadeado e que tipo de token ele aceita
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}