package com.savemystudies.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Permite CORS para todas as suas rotas /api
                .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:5173", "https://seusiteprod.com.br")
                // 👆 Coloque aqui a URL onde seu Frontend está rodando.
                // 3000 = React/Angular/Vue dev. 5173 = Vite/Next.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos que você usa
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Se você usa cookies/sessões
    }
}