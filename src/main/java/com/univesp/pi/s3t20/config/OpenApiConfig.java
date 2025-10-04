package com.univesp.pi.s3t20.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PI Univesp 2025 - Sistema de Vendas")
                        .description("API REST para gerenciamento de vendas, clientes, produtos e formas de pagamento")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe PI Univesp")
                                .email("pi@univesp.edu.br")
                                .url("https://univesp.edu.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.univesp.edu.br")
                                .description("Servidor de Produção")
                ));
    }
}
