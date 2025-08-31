package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "redacao") // Boa prática: nome no singular
public class Redacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    private String vestibular;

    private String tema;

    @Column(columnDefinition = "TEXT", nullable = false) // Mapeia para um tipo de texto longo
    private String textoUsuario;

    @Column(columnDefinition = "TEXT")
    private String feedbackGerado;

    private Double nota;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataEnvio;

    @PrePersist // Define a data de criação antes de salvar no banco
    protected void onCreate() {
        this.dataEnvio = LocalDateTime.now();
    }
}