package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;;
@Entity
@Table(name = "progresso_estudo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressoEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    private Double horasEstudadas;
    private Double percentualConclusao;
}
