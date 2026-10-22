package com.abhishek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                                "https://employment-frontend.vercel.app",
                                "https://employment-frontend-ebu8qv44c-nutblaster07s-projects.vercel.app"
                        )
                        .allowedMethods(
                                "GET", "POST", "PUT", "PATCH",
                                "DELETE", "OPTIONS"
                        )
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}