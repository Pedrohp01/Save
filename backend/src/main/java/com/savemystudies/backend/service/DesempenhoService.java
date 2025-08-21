package com.savemystudies.backend.service;

import com.savemystudies.backend.model.QuestaoUsuario;
import com.savemystudies.backend.repository.QuestaoUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class DesempenhoService {

    private final QuestaoUsuarioRepository repo;

    public DesempenhoService(QuestaoUsuarioRepository repo) {
        this.repo = repo;
    }

    public void registrarResposta(QuestaoUsuario questao) {
        questao.setAcertou(
                questao.getRespostaEscolhida().equalsIgnoreCase(questao.getRespostaCorreta())
        );
        repo.save(questao);
    }

   // public List<RelatorioArea> gerarRelatorioPorArea(Long usuarioId) {
        // agrupa por área e calcula % de acertos
        // retorna lista de objetos com nome da área, total de questões e % acerto
    }
