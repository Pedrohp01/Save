package com.savemystudies.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestClient rest;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public GeminiService(
            ObjectMapper objectMapper,
            @Value("${gemini.api.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.model:gemini-1.5-flash}") String model
    ) {
        this.rest = RestClient.builder().baseUrl(baseUrl).build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String gerarResumo(String area, String materia, String topico, String subtopico) {
        // A formatação do prompt agora está correta, usando a concatenação de strings.
        String prompt = String.format(
                "Formate a resposta da seguinte maneira:\n\n" +
                        "Crie um resumo claro e objetivo para vestibulandos sobre o subtema '%s'.\n" +
                        "O resumo deve estar dentro do contexto mais amplo de '%s', que está inserido na matéria '%s' e na área de conhecimento '%s'.\n\n" +
                        "O resumo deve conter informações relevantes e ser de fácil compreensão, focado para vestibular.\n\n" +
                        "Organize o resumo com títulos bem definidos, por exemplo:\n\n" +
                        "Título do tema\n" +
                        "Texto do resumo, de forma clara e concisa.\n\n" +
                        "Ao fim, coloque 'como tal assunto é cobrado nos vestibulares'.\n\n" +
                        "Não coloque 'dicas ou algo do tipo', apenas o resumo.\n",
                subtopico, topico, materia, area
        );
        return gerarResposta(prompt);
    }

    // --------- infraestrutura ----------
    private String gerarResposta(String prompt) {
        System.out.println("Prompt enviado: " + prompt);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                // 'role' é opcional, mas ajuda a manter compatibilidade
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );

        try {
            String raw = rest.post()
                    .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return extrairTexto(raw);

        } catch (RestClientException e) {
            System.err.println("Erro ao chamar Gemini: " + e.getMessage());
            // Retorna uma mensagem de erro mais descritiva para o front-end ou log
            return "Erro: Não foi possível gerar o conteúdo. Verifique sua conexão ou a API do Gemini.";
        }
    }

    private String extrairTexto(String raw) {
        try {
            JsonNode root = objectMapper.readTree(raw);

            // Trata resposta de erro vinda da API
            if (root.has("error")) {
                String msg = root.path("error").path("message").asText("Erro desconhecido da API.");
                return "Erro da API Gemini: " + msg;
            }

            return root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText("Erro: resposta inesperada do Gemini.");
        } catch (Exception e) {
            System.err.println("Erro ao processar resposta do Gemini: " + e.getMessage());
            return "Erro ao processar a resposta da API.";
        }
    }
}
