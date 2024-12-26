package com.pedro.apirest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Personas",
                description = "Esta API permite gestionar personas en el sistema.",
                version = "1.0.0"
//                contact = @Contact(name = "name", email = "email")
        )
)
public class OpenApiConfig {
}
