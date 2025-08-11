package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "questoes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;

    private String respostaCorreta;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;
}