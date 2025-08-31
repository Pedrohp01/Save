package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<MetaEstudo> metas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ResumoGerado> resumos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercicio> exercicios;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ProgressoEstudo> progresso;
}
