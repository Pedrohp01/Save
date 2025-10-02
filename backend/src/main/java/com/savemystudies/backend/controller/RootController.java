package com.savemystudies.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return "Bem-vindo ao SaveMyStudies API. Use endpoints como /resumos/gerar";
    }
}