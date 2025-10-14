package com.savemystudies.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para testes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",        // libera acesso à sua API
                                "/swagger-ui/**", // se estiver usando Swagger
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable()); // Desativa a tela de login padrão

        return http.build();
    }
}
