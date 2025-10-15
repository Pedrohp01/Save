package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "questoes_geradas")
public class QuestaoGerada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID usado pelo Front-end para correção

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_gerado_id", nullable = false)
    private ExercicioGerado exercicioGerado;

    // Campos da questão
    @Column(columnDefinition = "TEXT")
    private String textoBase;

    @Column(columnDefinition = "TEXT")
    private String enunciado;

    // As alternativas no formato JSON para serem reconstruídas pelo frontend
    @Column(columnDefinition = "TEXT")
    private String alternativasJson;

    // Dados críticos para a correção (não enviados ao front-end)
    @Column(length = 1)
    private String respostaCorreta;

    @Column(columnDefinition = "TEXT")
    private String explicacao;
}