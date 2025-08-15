package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;;
@Entity
@Table(name = "subtopico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subtopico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @OneToMany(mappedBy = "subtopico", cascade = CascadeType.ALL)
    private List<Tema> temas;
}
