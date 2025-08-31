package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<String> diasDaSemana = new ArrayList<>();

    private int minutospordia;  // minutos por dia de estudo
    private String vestibular;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Cronograma(String vestibular, ArrayList<String> diasDaSemana, double horasPorDia) {
        this.vestibular = vestibular;
        this.diasDaSemana = diasDaSemana;
        this.minutospordia = (int) (horasPorDia * 60);
    }
    @OneToMany(mappedBy = "cronograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CronogramaDia> dias = new ArrayList<>();

    @Lob // pode ser grande, então usamos LOB
    private String textoGerado;
}
