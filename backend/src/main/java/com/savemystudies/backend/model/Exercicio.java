package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materia;  // Ex: Matemática, Química, etc.
    private String enunciado;
    private String respostaUsuario;
    private String respostaCorreta;
    private Double nota; // pode ser porcentagem ou 0-10

    private LocalDateTime dataResolucao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

}
