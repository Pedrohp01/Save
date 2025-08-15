package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;;

@Entity
@Table(name = "meta_estudo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaEstudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    private Double horasPorDia;

    @Column(columnDefinition = "json")
    private String diasSemana;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private String status;

    @OneToMany(mappedBy = "meta", cascade = CascadeType.ALL)
    private List<RegistroEstudoDiario> registros;
}
