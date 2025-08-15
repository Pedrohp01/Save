package com.savemystudies.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evolucao_redacao")
public class EvolucaoRedacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "redacao_id", nullable = false)
    private Redacao redacao;

 // maybe, make a list of  "Redacão", some idea on my head :)

    @Lob
    private String textoCorrigido;

    @Lob
    private String comentarios;

    private LocalDateTime dataCorrecao = LocalDateTime.now();

    public LocalDateTime getDataCorrecao() {
        return dataCorrecao;
    }

    public void setDataCorrecao(LocalDateTime dataCorrecao) {
        this.dataCorrecao = dataCorrecao;
    }

    // Getters e setters...
}
