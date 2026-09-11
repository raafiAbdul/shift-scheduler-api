package com.projects.shift_scheduler_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "raafiAbdul",
                        email = "rafrafyunos@gmail.com"
                ),
                title = "Shift Scheduler API - raafiAbdul",
                description = "A  RESTful API for scheduling shifts using " +
                        "Spring Boot, Spring Data JPA and PostgreSQL."
        ),
        servers = @Server(
                description = "Local ENV",
                url = "http://localhost:8080"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Uses JWT Bearer token. Token is provided in the /api/v1/employee/login path. " +
                "You only need to put in your token, no need for \"Bearer ...\"."
)
public class OpenApiConfig {

}