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

    // ---------- gerar exercícios ----------
    public String gerarExercicio(String area, String materia, String topico, String subtopico) {
        String promptTemplate =  "Gere exatamente 5 questões de múltipla escolha no nível ENEM, " +
                "sobre o conteúdo informado, SEM usar imagens, gráficos ou fórmulas em LaTeX.\n\n" +
                "⚠️ Regras obrigatórias:\n" +
                "- Cada questão deve ter apenas 1 alternativa correta.\n" +
                "- Todas as alternativas devem ser plausíveis (não invente opções absurdas).\n" +
                "- Use linguagem clara, como em questões reais de vestibular.\n" +
                "- Mantenha o formato estritamente igual ao modelo abaixo, mas **não inclua a resposta correta no front**:\n\n" +
                "QUESTAO_1:\n" +
                "Enunciado da questão\n" +
                "A) Alternativa A\n" +
                "B) Alternativa B\n" +
                "C) Alternativa C\n" +
                "D) Alternativa D\n" +
                "E) Alternativa E\n\n" +
                "Repita para as 5 questões.\n\n" +
                "📚 Contexto:\n" +
                "Área: %s\n" +
                "Matéria: %s\n" +
                "Tópico: %s\n" +
                "Subtópico: %s";

        String promptEx = String.format(promptTemplate, area, materia, topico, subtopico);

        return gerarResposta(promptEx);
    }

    // ---------- gerar resumos ----------
    public String gerarResumo(String area, String materia, String topico, String subtopico) {
        String prompt = String.format(
                "Formate a resposta da seguinte maneira:\n\n" +
                        "Crie um resumo claro e objetivo para vestibulandos sobre o subtema '%s'.\n" +
                        "O resumo deve estar dentro do contexto mais amplo de '%s', que está inserido na matéria '%s' e na área de conhecimento '%s'.\n\n" +
                        "O resumo deve conter informações relevantes e ser de fácil compreensão, focado para vestibular.\n\n" +
                        "Organize o resumo com títulos bem definidos, por exemplo:\n\n" +
                        "Título do tema\n" +
                        "Texto do resumo.\n\n" +
                        "Ao fim, coloque 'como tal assunto é cobrado nos vestibulares'.",
                subtopico, topico, materia, area
        );
        return gerarResposta(prompt);
    }
    public String gerarCronograma(String vestibular, double vezesPorSemana, double horasPorDia) {
        String prompt = String.format(
                "Monte um cronograma de estudos personalizado e completo para o vestibular: %s.\n\n" +
                        "Regras:\n" +
                        "- O usuário estuda %.0f vezes por semana.\n" +
                        "- Cada sessão de estudo tem aproximadamente %.1f horas.\n" +
                        "- O cronograma deve cobrir todas as áreas exigidas nesse vestibular.\n" +
                        "- Organize os estudos de forma progressiva: do básico ao avançado.\n" +
                        "- Divida por áreas, matérias e tópicos.\n" +
                        "- Inclua revisões periódicas e simulados.\n" +
                        "- Seja específico: indique exatamente quais conteúdos o estudante deve revisar em cada etapa.\n\n" +
                        "Formato esperado da resposta:\n" +
                        "SEMANA X:\n" +
                        "  • Dia da semana\n" +
                        "     - Área: ...\n" +
                        "     - Disciplina: ...\n" +
                        "     - Tópicos a estudar: ...\n" +
                        "     - Tempo estimado: ...\n\n" +
                        "Finalize com um resumo semanal destacando revisões, simulados e avanços alcançados.",
                vestibular, vezesPorSemana, horasPorDia
        );

        return prompt;
    }


    // ---------- infraestrutura ----------
    private String gerarResposta(String prompt) {
        System.out.println("Prompt enviado: " + prompt);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
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
            return "Erro: Não foi possível gerar o conteúdo. Verifique sua conexão ou a API do Gemini.";
        }
    }

    private String extrairTexto(String raw) {
        try {
            JsonNode root = objectMapper.readTree(raw);

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
