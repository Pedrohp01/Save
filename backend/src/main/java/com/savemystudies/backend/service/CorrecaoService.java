package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.FeedbackUnicoResponse;
import com.savemystudies.backend.exceptions.QuestaoNaoEncontradaException;
import com.savemystudies.backend.model.QuestaoGerada;
import com.savemystudies.backend.repository.QuestaoGeradaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CorrecaoService {

    private final QuestaoGeradaRepository questaoRepository;

    /**
     * Corrige uma questão de resposta única, comparando a resposta do usuário
     * com o gabarito e retornando o feedback detalhado (incluindo a explicação).
     *
     * @param questaoId O ID da questão sendo corrigida.
     * @param respostaUsuario A resposta fornecida pelo usuário.
     * @return FeedbackUnicoResponse contendo o resultado (acerto ou erro) e a explicação.
     * @throws QuestaoNaoEncontradaException se o ID da questão não for encontrado.
     */
    public FeedbackUnicoResponse corrigirQuestaoUnica(Long questaoId, String respostaUsuario) throws QuestaoNaoEncontradaException {

        // 1. Busca a questão pelo ID (que tem a resposta correta e explicação)
        QuestaoGerada questao = questaoRepository.findById(questaoId)
                // CORREÇÃO: Lança a exceção customizada esperada pelo Controller
                .orElseThrow(() -> new QuestaoNaoEncontradaException("Questão com ID " + questaoId + " não encontrada."));

        // 2. Padroniza as respostas para comparação (ex: 'a' vs 'A')
        // OBS: Esta lógica de comparação simples é ideal para questões de Múltipla Escolha (A, B, C, etc.)
        String correta = questao.getRespostaCorreta().toUpperCase(Locale.ROOT);
        String usuario = respostaUsuario.toUpperCase(Locale.ROOT);

        boolean acertou = correta.equals(usuario);

        // 3. Retorna o feedback
        // O FeedbackUnicoResponse contém se acertou, qual era a resposta correta, e a explicação.
        return new FeedbackUnicoResponse(
                acertou,
                questao.getRespostaCorreta(),
                questao.getExplicacao()
        );
    }
}
