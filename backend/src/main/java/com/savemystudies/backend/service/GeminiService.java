package com.savemystudies.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        // Constrói o WebClient com a URL base, injetado pelo Spring
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta").build();
        this.objectMapper = objectMapper;
    }

    public Mono<String> gerarResposta(String prompt) {
        String body = "{ \"contents\": [ { \"parts\":[{\"text\":\"" + prompt + "\"}] } ] }";

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/models/gemini-pro:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build())
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extrairTextoDaResposta)
                .onErrorResume(e -> {
                    System.err.println("Erro ao chamar a API do Gemini: " + e.getMessage());
                    return Mono.just("Erro: Não foi possível gerar o conteúdo.");
                });
    }

    private String extrairTextoDaResposta(String resposta) {
        try {
            JsonNode root = objectMapper.readTree(resposta);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            return textNode.asText();
        } catch (Exception e) {
            System.err.println("Erro ao extrair texto da resposta do Gemini: " + e.getMessage());
            return "Erro ao processar a resposta da API.";
        }
    }

    // 🔹 Métodos especializados (agora usam Mono<String>)

    public Mono<String> gerarResumo(String area, String materia, String topico) {
        String prompt = String.format(
                "Gere um resumo didático e objetivo sobre o tópico '%s' da matéria '%s' da área '%s', no estilo de preparação para o ENEM.",
                topico, materia, area
        );
        return gerarResposta(prompt);
    }

    public Mono<String> gerarQuestao(String area, String materia, String topico) {
        String prompt = String.format(
                "Crie uma questão no estilo ENEM sobre o tópico '%s' da matéria '%s' da área '%s', com alternativas e a resposta correta.",
                topico, materia, area
        );
        return gerarResposta(prompt);
    }

    public Mono<String> gerarExplicacao(String area, String materia, String topico) {
        String prompt = String.format(
                "Explique de forma passo a passo o tópico '%s' da matéria '%s' da área '%s', como se fosse para um aluno do ensino médio.",
                topico, materia, area
        );
        return gerarResposta(prompt);
    }
}