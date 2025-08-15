package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;;
@Entity
@Table(name = "exercicio_gerado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExercicioGerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "tema_id", nullable = false)
    private Tema tema;

    @Column(columnDefinition = "json")
    private String conteudo;

    private LocalDateTime dataGeracao = LocalDateTime.now();
}

