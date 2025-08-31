package com.savemystudies.backend.dto;

import com.savemystudies.backend.model.Exercicio;
import com.savemystudies.backend.model.Redacao;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDesempenho {

    private String nomeUsuario;
    private Double mediaExercicios;
    private Double mediaRedacoes;
    private List<Exercicio> exerciciosRecentes;
    private List<Redacao> redacoesRecentes;

}