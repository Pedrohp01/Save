package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter @NoArgsConstructor
public class QuestaoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User usuario;

    private String area;
    private String materia;
    private String topico;
    private String subtopico;

    private String enunciado;
    private String respostaEscolhida; // letra escolhida pelo usuário
    private String respostaCorreta;   // só para referência interna

    private boolean acertou;
}
