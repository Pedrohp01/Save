package com.savemystudies.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;
    private String alternativaCorreta;

    @ElementCollection
    private List<String> alternativas;

    // Relaciona cada questão com um usuário
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private User usuario;
}
