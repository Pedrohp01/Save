package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vestibularOuCurso; // ENEM, FUVEST, Java, etc.

    private int vezesPorSemana;
    private double horasPorDia;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CronogramaSemana> semanas = new ArrayList<>();
}
