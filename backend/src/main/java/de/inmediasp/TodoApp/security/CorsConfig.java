package de.inmediasp.TodoApp.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@ConditionalOnProperty(name = "de.inmediasp.TodoApp.app.corsEnvironment", havingValue = "true")
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.err.println("Cors mapping added");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001/")
                .allowedMethods("*");

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.err.println("Cors source added");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3001/"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
