package com.savemystudies.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savemystudies.backend.dto.RedacaoFeedback;
import com.savemystudies.backend.model.Cronograma;
import com.savemystudies.backend.model.CronogramaDia;
import com.savemystudies.backend.repository.CronogramaDiaRepository;
import com.savemystudies.backend.repository.CronogramaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class GeminiService {

    private final RestClient rest;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final CronogramaRepository cronogramaRepository;
    private final CronogramaDiaRepository cronogramaDiaRepository;

    public GeminiService(
            ObjectMapper objectMapper,
            @Value("${gemini.api.base-url}") String baseUrl,
            @Value("${gemini.api.model}") String model,
            @Value("${gemini.api.key}") String apiKey,
            CronogramaRepository cronogramaRepository,
            CronogramaDiaRepository cronogramaDiaRepository
    ) {
        this.rest = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();

        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.cronogramaRepository = cronogramaRepository;
        this.cronogramaDiaRepository = cronogramaDiaRepository;
    }

    // ---------- gerar exercícios ----------
    private static final String EXERCICIO_PROMPT = """
Gere exatamente 5 questões de múltipla escolha no nível ENEM
sobre o conteúdo informado, SEM usar imagens, gráficos ou fórmulas em LaTeX.

⚠️ Regras obrigatórias:
- Cada questão deve ter apenas 1 alternativa correta.
- Todas as alternativas devem ser plausíveis.
- Use linguagem clara, como em questões reais de vestibular.
- Formato esperado:

QUESTAO_1:
Enunciado da questão
A) Alternativa A
B) Alternativa B
C) Alternativa C
D) Alternativa D
E) Alternativa E
Resposta correta: [letra]
Explicação: [texto explicando o motivo da resposta]

Repita para as 5 questões.

📚 Contexto:
Área: %s
Matéria: %s
Tópico: %s
Subtópico: %s
""";

    public String gerarExercicio(String area, String materia, String topico, String subtopico) {
        String prompt = String.format(EXERCICIO_PROMPT, area, materia, topico, subtopico);
        return gerarResposta(prompt);
    }

    // ---------- gerar resumos ----------
    public String gerarResumo(String area, String materia, String topico, String subtopico) {
        String prompt = String.format("""
                Crie um resumo claro e objetivo para vestibulandos sobre o subtema '%s'.
                O resumo deve estar dentro do contexto de '%s', na matéria '%s', área '%s'.

                Organize o resumo com títulos bem definidos e finalize com:
                "como tal assunto é cobrado nos vestibulares".
                """, subtopico, topico, materia, area);
        return gerarResposta(prompt);
    }

    // ---------- gerar cronograma ----------
    public Cronograma gerarCronograma(Cronograma cronograma) {
        double horasPorDia = cronograma.getMinutospordia() / 60.0;

        String prompt = String.format("""
                Monte um cronograma de estudos para o vestibular: %s.

                O cronograma deve iniciar em %s e terminar no dia anterior ao vestibular (%s).
                Regras:
                - Dias de estudo: %s.
                - Cada sessão: %.1f horas.
                - Progressão: básico ao avançado.
                - Inclua revisões, simulados e planejamento final.
                """,
                cronograma.getVestibular(),
                cronograma.getDataInicio(),
                cronograma.getDataFim(),
                cronograma.getDiasDaSemana(),
                horasPorDia);

        String resposta = gerarResposta(prompt);
        cronograma.setTextoGerado(resposta);
        Cronograma salvo = cronogramaRepository.save(cronograma);

        // gerar dias
        LocalDate data = cronograma.getDataInicio();
        while (!data.isAfter(cronograma.getDataFim())) {
            if (cronograma.getDiasDaSemana().contains(data.getDayOfWeek())) {
                CronogramaDia dia = new CronogramaDia();
                dia.setCronograma(salvo);
                dia.setData(data);
                dia.setDescricao("Estudo no dia " + data);
                dia.setConcluido(false);
                cronogramaDiaRepository.save(dia);
            }
            data = data.plusDays(1);
        }
        return salvo;
    }

    // ---------- redação ----------
    public RedacaoFeedback analisarRedacao(String texto, String vestibular, String tema) {
        String prompt = String.format("""
            Analise a redação sobre o tema "%s" (%s).
            - Avalie pontos fortes e fracos.
            - Nota de 0 a 1000.
            - Feedback por competência.
            - Sugestões práticas de melhoria.

            Texto: "%s"

            Retorne JSON: {"feedback": "...", "nota": 0.0}
            """, tema, vestibular, texto);

        try {
            String respostaBruta = gerarResposta(prompt);
            String jsonPuro = respostaBruta
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            return objectMapper.readValue(jsonPuro, RedacaoFeedback.class);

        } catch (Exception e) {
            log.error("Erro ao processar redação", e);
            return new RedacaoFeedback("Erro ao analisar a redação", 0.0);
        }
    }

    public List<String> gerarTemasRedacao() {
        String prompt = """
                Gere 1 tema de redação no modelo ENEM, sobre atualidades.
                Retorne JSON: {"temas": ["tema aqui"]}
                """;

        try {
            String resposta = gerarResposta(prompt);
            JsonNode root = objectMapper.readTree(resposta);
            JsonNode temasNode = root.path("temas");

            if (temasNode.isArray()) {
                List<String> temas = new ArrayList<>();
                temasNode.forEach(t -> temas.add(t.asText()));
                return temas;
            }
        } catch (Exception e) {
            log.error("Erro ao processar temas de redação", e);
        }
        return Collections.emptyList();
    }

    // ---------- integração com a API Gemini ----------
    private String gerarResposta(String prompt) {
        log.info("Enviando prompt: {}", prompt);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        try {
            String raw = rest.post()
                    // ✅ Corrigido: Gemini usa query param `?key=` (não Authorization)
                    .uri("/models/" + model + ":generateContent?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            log.info("Resposta bruta da API Gemini: {}", raw);
            return extrairTexto(raw);

        } catch (RestClientException e) {
            log.error("Erro ao chamar Gemini", e);
            throw new RuntimeException("Falha ao gerar conteúdo", e);
        }
    }

    private String extrairTexto(String raw) {
        try {
            JsonNode root = objectMapper.readTree(raw);
            if (root.has("error")) {
                String msg = root.path("error").path("message").asText("Erro desconhecido da API.");
                throw new RuntimeException("Erro da API Gemini: " + msg);
            }
            return root.path("candidates")
                    .path(0).path("content")
                    .path("parts").path(0)
                    .path("text").asText("Resposta inesperada da API.");
        } catch (Exception e) {
            log.error("Erro ao processar resposta bruta do Gemini", e);
            throw new RuntimeException("Erro ao processar resposta da API", e);
        }
    }
}
