package com.savemystudies.backend.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;;
@Entity
@Table(name = "resumo_gerado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoGerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @Lob
    private String conteudo;

    private LocalDateTime dataGeracao = LocalDateTime.now();
}