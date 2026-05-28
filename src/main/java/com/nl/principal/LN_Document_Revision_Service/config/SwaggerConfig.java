package com.nl.principal.LN_Document_Revision_Service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by Peter Maunatlala on 2026/05/27.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(apiInfo())
                .select()
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Principal Software Engineer Assignment")
                .description("Legal Document Revision Service — Spring Boot Message Broker Implementation")
                .license("Apache License Version 2.0")
                .version("2.0")
                .build();
    }

}
