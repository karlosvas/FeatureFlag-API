package com.equipo01.featureflag.featureflag.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration class.
 * This class configures automatic API documentation using Swagger.
 */
@Configuration
public class SwaggerConfig {
/**
 * Configures the general documentation of the API using OpenAPI.
 * Also configures the JWT (Bearer Token) security scheme that will be used to access protected endpoints.
 *
 * @return an instance of {@link OpenAPI} with the API configuration.
 */
@Bean
public OpenAPI customOpenAPI() {
return new OpenAPI()
        .info(new Info()
                .title("FeatureFlag-API") // Title of the API
                .version("1.0") // Version of the API
                .description("API documentation for FeatureFlag-API")) // Description of the API
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
}
}