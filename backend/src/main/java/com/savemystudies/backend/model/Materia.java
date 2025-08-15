package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "materia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private List<Topico> topicos;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL)
    private List<Subtopico> subtopicos;
}

