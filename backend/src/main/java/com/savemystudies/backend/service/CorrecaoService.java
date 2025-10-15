package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.FeedbackUnicoResponse;
import com.savemystudies.backend.model.QuestaoGerada;
import com.savemystudies.backend.repository.QuestaoGeradaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CorrecaoService {

    private final QuestaoGeradaRepository questaoRepository;

    public FeedbackUnicoResponse corrigirQuestaoUnica(Long questaoId, String respostaUsuario) {

        // 1. Busca a questão pelo ID (que tem a resposta correta e explicação)
        QuestaoGerada questao = questaoRepository.findById(questaoId)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada."));

        // 2. Padroniza as respostas para comparação (ex: 'a' vs 'A')
        String correta = questao.getRespostaCorreta().toUpperCase(Locale.ROOT);
        String usuario = respostaUsuario.toUpperCase(Locale.ROOT);

        boolean acertou = correta.equals(usuario);

        // 3. Retorna o feedback
        return new FeedbackUnicoResponse(
                acertou,
                questao.getRespostaCorreta(),
                questao.getExplicacao()
        );
    }
}