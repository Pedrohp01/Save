package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;;

@Entity
@Table(name = "tema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "subtopico_id", nullable = false)
    private Subtopico subtopico;

    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
    private List<ExercicioGerado> exercicios;
}
