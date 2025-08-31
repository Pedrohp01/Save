package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.RedacaoFeedback;
import com.savemystudies.backend.model.Redacao;
import com.savemystudies.backend.model.User;
import com.savemystudies.backend.repository.RedacaoRepository;
import com.savemystudies.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class RedacaoService {

    private final GeminiService geminiService;
    private final RedacaoRepository redacaoRepository;
    private final UserRepository userRepository;

    @Autowired
    public RedacaoService(GeminiService geminiService, RedacaoRepository redacaoRepository, UserRepository userRepository) {
        this.geminiService = geminiService;
        this.redacaoRepository = redacaoRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retorna uma lista de temas de redação pré-prontos.
     * Não há interação com o Gemini, os temas são fixos no código.
     */
    public List<String> getTemasProntos() {
        return Arrays.asList(
                "O combate à invisibilidade e ao trabalho de cuidado realizado pela mulher.",
                "Desafios para a formação educacional de surdos no Brasil.",
                "A importância de uma alimentação saudável para o bem-estar da sociedade."
        );
    }

    /**
     * Chama o GeminiService para gerar uma lista de temas de redação dinamicamente.
     */
    public List<String> gerarTemas() {
        return geminiService.gerarTemasRedacao();
    }

    public Redacao gerarFeedbackESalvar(Redacao redacao, Long userId) {
        // Busca o usuário
        User usuario = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));
        redacao.setUsuario(usuario);

        // Adicione um log para ver o que está sendo enviado para o Gemini
        System.out.println("Enviando para o Gemini - Tema: " + redacao.getTema() + ", Vestibular: " + redacao.getVestibular() + ", Texto: " + redacao.getTextoUsuario());

        // Chama o GeminiService para analisar a redação
        RedacaoFeedback feedback = geminiService.analisarRedacao(
                redacao.getTextoUsuario(),
                redacao.getVestibular(),
                redacao.getTema()
        );

        // Adicione um log para ver o que o Gemini retornou
        System.out.println("Resposta do Gemini - Nota: " + feedback.getNota() + ", Feedback: " + feedback.getFeedback());

        // Atribui a nota e o feedback ao objeto Redacao
        redacao.setNota(feedback.getNota());
        redacao.setFeedbackGerado(feedback.getFeedback());
        redacao.setDataEnvio(LocalDateTime.now());

        // Salva a redação completa no banco de dados
        Redacao redacaoSalva = redacaoRepository.save(redacao);

        // Adicione um log para ver o que foi salvo no banco e o que será retornado
        System.out.println("Redação salva - ID: " + redacaoSalva.getId() + ", Nota: " + redacaoSalva.getNota());

        return redacaoSalva;
    }
}