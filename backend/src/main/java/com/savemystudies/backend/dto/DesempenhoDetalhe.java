package com.savemystudies.backend.dto;
public record DesempenhoDetalhe(
        String nome,        // Nome da Matéria, Tópico ou Subtópico
        Long totalQuestoes,
        Long acertos,
        Double percentualAcerto
) {}