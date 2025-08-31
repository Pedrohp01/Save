package com.savemystudies.backend.dto;

import lombok.Data;

// Dados que vêm do formulário de cadastro do usuário
@Data
public class UserRegistrationDto {
    private String nome;
    private String email;
    private String senha; // Senha em texto puro
}