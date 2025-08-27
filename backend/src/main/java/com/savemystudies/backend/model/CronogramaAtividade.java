package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaAtividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;       // Ex: Ciências Humanas, Programação
    private String disciplina; // Ex: História, Java Básico
    private String topicos;    // Ex: Revolução Francesa, POO - Herança
    private double tempoEstimado; // em horas

    private boolean concluida = false;

    @ManyToOne
    private CronogramaDia dia;
}
