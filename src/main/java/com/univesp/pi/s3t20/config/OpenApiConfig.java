package com.univesp.pi.s3t20.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PI Univesp 2025 - Sistema de Vendas",
                version = "1.0.0",
                description = "API REST para gerenciamento de vendas, clientes, produtos e formas de pagamento",
                contact = @Contact(
                        name = "Equipe PI Univesp",
                        email = "pi@univesp.edu.br",
                        url = "https://univesp.edu.br"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "${pi-univesp.openapi.server-url:http://localhost:8080}")
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components());
    }
}
