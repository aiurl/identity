package com.nerosoft.linkyou.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger", "/swagger/");
    }

    @Bean
    public OpenAPI linkyouOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Linkyou Identity API")
                        .description("User authentication and identity service for Linkyou")
                        .version("v1.0.0")
                        .contact(new Contact().name("Nerosoft").url("https://theurl.io"))
                        .license(new License().name("MIT")));
    }
}
