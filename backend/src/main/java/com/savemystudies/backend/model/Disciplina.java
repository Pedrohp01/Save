package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Cronograma> cronogramas;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Questao> questoes;
}
