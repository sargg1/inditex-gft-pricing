package com.inditex.pricing.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pricing Service API",
                version = "v1",
                description = "REST API that returns the applicable price for a product and brand on a given request date."
        )
)
public class OpenApiConfiguration {
}
