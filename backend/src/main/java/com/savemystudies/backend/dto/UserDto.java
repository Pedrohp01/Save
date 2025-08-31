package com.savemystudies.backend.dto;

import lombok.Data;

@Data
public class UserDto {
    private String nome;
    private String email;
    private String senha;
}