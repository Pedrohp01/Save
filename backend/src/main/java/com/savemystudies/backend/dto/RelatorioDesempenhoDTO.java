package com.savemystudies.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioDesempenhoDTO {
    private String nome;          // Área, disciplina, tópico ou subtopico
    private Long totalQuestoes;
    private Long totalAcertos;
    private Double percentualAcerto;
}
