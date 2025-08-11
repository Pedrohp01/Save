package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
    @Table(name = "cronogramas")
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Cronograma {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDateTime dataEstudo;

        private String descricao;

        @ManyToOne
        @JoinColumn(name = "disciplina_id")
        private Disciplina disciplina;
    }

