package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;;

@Entity
@Table(name = "registro_estudo_diario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroEstudoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meta_id", nullable = false)
    private MetaEstudo meta;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    private LocalDate data;
    private Double horasEstudadas;
    private String status;
}
