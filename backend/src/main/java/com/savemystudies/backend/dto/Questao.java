package com.savemystudies.backend.dto;

import java.util.Map;

// Questão enviada ao cliente. Note a ausência de 'respostaCorreta' e 'explicacao'.
public record Questao(
        Long id, // ID da QuestaoGerada no BD
        String textoBase,
        String enunciado,
        Map<String, String> alternativas
) {}