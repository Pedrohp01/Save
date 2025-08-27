package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestaoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId; // ID do usuário que respondeu

    @Column(columnDefinition = "TEXT")
    private String questao; // enunciado da questão

    private String alternativaSelecionada; // letra escolhida pelo usuário (A, B, C, D, E)

    private String correta; // letra correta da questão

    private boolean acertou; // indica se o usuário acertou a questão

    // Contexto da questão (para relatórios)
    private String area;
    private String materia;
    private String topico;
    private String subtopico;
}
