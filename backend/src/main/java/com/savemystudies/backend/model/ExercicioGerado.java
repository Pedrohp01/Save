package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "exercicios_gerados")
public class ExercicioGerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assumindo que você já tem a entidade Subtopico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtopico_id", nullable = false)
    private Subtopico subtopico;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "exercicioGerado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestaoGerada> questoes;

    @Column(columnDefinition = "TEXT")
    private String promptUsado;
}