package com.savemystudies.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permite acesso público ao endpoint de cadastro de usuários
                                .requestMatchers("/usuarios/cadastro").permitAll()
                                // Adiciona o endpoint de feedback de redação para acesso público
                                .requestMatchers("/redacoes/feedback/**").permitAll()
                                // Protege todas as outras rotas, exigindo autenticação
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());
        return http.build();
    }
}