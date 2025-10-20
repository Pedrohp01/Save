package com.savemystudies.backend.dto;

import java.util.Map;

public record QuestaoSave(
        Long id,
        String textoBase,
        String enunciado,
        Map<String, String> alternativas
) {}