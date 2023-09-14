package com.example.shelter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("GET", "PUT")
                .allowedOrigins(
                        "http://www.localhost:3000",
                        "http://localhost:3000",
                        "https://www.shere.link"
                );
    }

}
