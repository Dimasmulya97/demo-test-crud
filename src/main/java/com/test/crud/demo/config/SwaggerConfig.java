//package com.test.crud.demo.config;
//
//import com.test.crud.demo.constant.Constant;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@OpenAPIDefinition(
//        info = @io.swagger.v3.oas.annotations.info.Info(
//                title = "My API",
//                version = "1.0",
//                description = "API Documentation"
//        )
//)
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // Konfigurasi skema keamanan
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList("bearerAuth");
//
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth", securityScheme))
//                .addSecurityItem(securityRequirement)
//                .info(new Info()
//                        .title("API Documentation")
//                        .version("1.0")
//                        .description("API Documentation using SpringDoc"));
//
//    }
//}
