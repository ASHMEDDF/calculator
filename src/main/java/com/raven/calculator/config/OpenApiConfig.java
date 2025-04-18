package com.raven.calculator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI calculatorOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Calculator REST API")
                        .description("API REST de Calculadora con JWT y persistencia de operaciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ashmed Diaz")
                                .email("ashmeddiaz5@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}