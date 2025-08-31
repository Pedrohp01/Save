package com.savemystudies.backend.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String senha;
}