package com.savemystudies.backend.dto;

// DTO para receber a resposta de uma única questão
public record RespostaUnicaDTO(
        Long questaoId,    // ID da questão sendo respondida
        String resposta   // Alternativa escolhida (ex: "A", "B")
) {}