package com.savemystudies.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "avaliacao")
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private List<QuestaoUsuario> respostasUsuario;
    private double nota;
    private int totalAcertos;
    private int totalQuestoes;

    public Avaliacao(List<QuestaoUsuario> respostasUsuario) {
        this.respostasUsuario = respostasUsuario;
        this.totalQuestoes = respostasUsuario != null ? respostasUsuario.size() : 0;
        this.totalAcertos = 0;
        this.nota = 0.0;
    }

    /**
     * Realiza a correção da avaliação.
     * Este método verifica para cada questão se a alternativa selecionada pelo usuário
     * é igual à alternativa correta e atualiza o status 'acertou' do objeto.
     * Ao final, calcula a nota total.
     */
    public void corrigir() {
        if (this.respostasUsuario == null || this.respostasUsuario.isEmpty()) {
            return; // Não há o que corrigir
        }

        int acertos = 0;
        for (QuestaoUsuario resposta : this.respostasUsuario) {
            boolean acertou = resposta.getCorreta().equalsIgnoreCase(resposta.getAlternativaSelecionada());
            resposta.setAcertou(acertou);
            if (acertou) {
                acertos++;
            }
        }
        this.totalAcertos = acertos;
        calcularNota();
    }

    /**
     * Calcula a nota com base no número de acertos.
     * A nota é calculada usando uma regra de três simples, onde o total de questões
     * representa a nota máxima de 10.
     */
    private void calcularNota() {
        if (this.totalQuestoes > 0) {
            this.nota = (double) (this.totalAcertos * 10) / this.totalQuestoes;
        } else {
            this.nota = 0.0;
        }
    }
}