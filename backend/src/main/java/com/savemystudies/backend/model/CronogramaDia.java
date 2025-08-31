package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class CronogramaDia {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "cronograma_id", nullable = false)
        private Cronograma cronograma;

        private LocalDate data;

        private String descricao;

        private boolean concluido = false;
    }


