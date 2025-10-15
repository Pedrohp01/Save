package com.savemystudies.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savemystudies.backend.dto.RedacaoFeedback;
import com.savemystudies.backend.dto.ExercicioResponse;
import com.savemystudies.backend.dto.Questao;
import com.savemystudies.backend.model.Cronograma;
import com.savemystudies.backend.model.CronogramaDia;
import com.savemystudies.backend.model.ExercicioGerado;
import com.savemystudies.backend.model.QuestaoGerada;
import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.repository.CronogramaDiaRepository;
import com.savemystudies.backend.repository.CronogramaRepository;
import com.savemystudies.backend.repository.ExercicioGeradoRepository;
import com.savemystudies.backend.repository.QuestaoGeradaRepository;
import com.savemystudies.backend.repository.SubtopicoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class GeminiService {

    // DTOs INTERNOS para mapear a resposta bruta do Gemini (com as respostas corretas)
    private record QuestaoInput(
            String textoBase,
            String enunciado,
            Map<String, String> alternativas,
            String respostaCorreta,
            String explicacao
    ) {}

    private record ExercicioInput(
            List<QuestaoInput> questoes
    ) {}

    private final RestClient rest;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    // Repositórios Cronograma (existentes)
    private final CronogramaRepository cronogramaRepository;
    private final CronogramaDiaRepository cronogramaDiaRepository;

    // NOVOS REPOSITÓRIOS INJETADOS
    private final ExercicioGeradoRepository exercicioGeradoRepository;
    private final QuestaoGeradaRepository questaoGeradaRepository;
    private final SubtopicoRepository subtopicoRepository;

    public GeminiService(
            ObjectMapper objectMapper,
            @Value("${gemini.api.base-url}") String baseUrl,
            @Value("${gemini.api.model}") String model,
            @Value("${gemini.api.key}") String apiKey,
            // Injeções existentes:
            CronogramaRepository cronogramaRepository,
            CronogramaDiaRepository cronogramaDiaRepository,
            // NOVAS Injeções:
            ExercicioGeradoRepository exercicioGeradoRepository,
            QuestaoGeradaRepository questaoGeradaRepository,
            SubtopicoRepository subtopicoRepository
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

        // Atribuição dos NOVOS REPOSITÓRIOS
        this.exercicioGeradoRepository = exercicioGeradoRepository;
        this.questaoGeradaRepository = questaoGeradaRepository;
        this.subtopicoRepository = subtopicoRepository;
    }

    // ---------- PROMPT PARA EXERCÍCIOS (Com Campo textoBase) ----------
    private static final String EXERCICIO_PROMPT = """
Gere exatamente 5 questões de múltipla escolha no nível ENEM.
Inclua, se apropriado, um 'textoBase' (trecho de obra, gráfico, tabela) sobre o conteúdo.
SEM usar imagens, gráficos ou fórmulas em LaTeX.

⚠️ Regras obrigatórias:
- Cada questão deve ter apenas 1 alternativa correta.
- Todas as alternativas devem ser plausíveis.
- Use linguagem clara, como em questões reais de vestibular.
- Retorne SOMENTE JSON puro, sem markdown, sem texto extra.

📚 Contexto:
Área: %s
Matéria: %s
Tópico: %s
Subtópico: %s

🧩 Formato exato esperado:
{
  "questoes": [
    {
      "textoBase": "Trecho de texto, citação, ou dados que a questão usará.",
      "enunciado": "texto da questão principal, referenciando o textoBase",
      "alternativas": {
        "A": "texto da alternativa A",
        "B": "texto da alternativa B",
        "C": "texto da alternativa C",
        "D": "texto da alternativa D",
        "E": "texto da alternativa E"
      },
      "respostaCorreta": "A",
      "explicacao": "texto explicando o motivo"
    }
  ]
}
""";

    // MÉTODO: Geração, Persistência e Retorno do DTO (com IDs)
    @Transactional
    public ExercicioResponse gerarExercicio(Long subtopicoId, String area, String materia, String topico, String subtopico) {
        String prompt = String.format(EXERCICIO_PROMPT, area, materia, topico, subtopico);

        try {
            ExercicioInput exercicioInput = gerarJson(prompt, ExercicioInput.class);
            return salvarEConverter(subtopicoId, exercicioInput, prompt);
        } catch (Exception e) {
            log.error("Erro no fluxo de geração e persistência do exercício", e);
            throw new RuntimeException("Falha ao gerar e salvar exercício.", e);
        }
    }

    // LÓGICA DE PERSISTÊNCIA
    private ExercicioResponse salvarEConverter(Long subtopicoId, ExercicioInput input, String promptUsado) {
        Subtopico subtopico = subtopicoRepository.findById(subtopicoId)
                .orElseThrow(() -> new RuntimeException("Subtópico não encontrado durante a geração."));

        // 1. Cria a entidade de cabeçalho
        ExercicioGerado novoExercicio = new ExercicioGerado();
        novoExercicio.setSubtopico(subtopico);
        novoExercicio.setPromptUsado(promptUsado);
        ExercicioGerado exercicioSalvo = exercicioGeradoRepository.save(novoExercicio);

        List<Questao> questoesParaCliente = new ArrayList<>();

        // 2. Itera sobre cada questão
        for (QuestaoInput qInput : input.questoes()) {
            QuestaoGerada questao = new QuestaoGerada();
            questao.setExercicioGerado(exercicioSalvo);
            questao.setTextoBase(qInput.textoBase());
            questao.setEnunciado(qInput.enunciado());

            try {
                questao.setAlternativasJson(objectMapper.writeValueAsString(qInput.alternativas()));
            } catch (Exception e) {
                log.error("Erro ao serializar alternativas", e);
                throw new RuntimeException("Falha ao salvar alternativas.", e);
            }

            // SALVA DADOS CRÍTICOS (Resposta e Explicação)
            questao.setRespostaCorreta(qInput.respostaCorreta().toUpperCase());
            questao.setExplicacao(qInput.explicacao());

            QuestaoGerada questaoSalva = questaoGeradaRepository.save(questao);

            // 3. Converte para o DTO de Saída (incluindo o ID)
            questoesParaCliente.add(new Questao(
                    questaoSalva.getId(),
                    questaoSalva.getTextoBase(),
                    questaoSalva.getEnunciado(),
                    qInput.alternativas()
            ));
        }

        return new ExercicioResponse(questoesParaCliente);
    }

    // ---------- gerar resumos (inalterado) ----------
    public String gerarResumo(String area, String materia, String topico, String subtopico) {
        String prompt = String.format("""
                Crie um resumo claro e objetivo para vestibulandos sobre o subtema '%s'.
                O resumo deve estar dentro do contexto de '%s', na matéria '%s', área '%s'.

                Organize o resumo com títulos bem definidos e finalize com:
                "como tal assunto é cobrado nos vestibulares".
                """, subtopico, topico, materia, area);
        return gerarResposta(prompt);
    }

    // ---------- gerar cronograma (inalterado) ----------
    public Cronograma gerarCronograma(Cronograma cronograma) {
        double horasPorDia = cronograma.getMinutospordia() / 60.0;
        // ... (lógica inalterada de cronograma) ...
        String prompt = String.format("""
                Monte um cronograma de estudos para o vestibular: %s.
                // ... (Regras de prompt) ...
                """,
                cronograma.getVestibular(),
                cronograma.getDataInicio(),
                cronograma.getDataFim(),
                cronograma.getDiasDaSemana(),
                horasPorDia);

        String resposta = gerarResposta(prompt);
        cronograma.setTextoGerado(resposta);
        Cronograma salvo = cronogramaRepository.save(cronograma);

        // gerar dias automaticamente
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

    // ---------- redação (inalterado) ----------
    public RedacaoFeedback analisarRedacao(String texto, String vestibular, String tema) {
        // ... (lógica inalterada de redação) ...
        String prompt = String.format("""
            Analise a redação sobre o tema "%s" (%s).
            // ... (Regras de prompt) ...
            Texto: "%s"
            """, tema, vestibular, texto);

        try {
            return gerarJson(prompt, RedacaoFeedback.class);
        } catch (Exception e) {
            log.error("Erro ao processar redação", e);
            return new RedacaoFeedback("Erro ao analisar a redação", 0.0);
        }
    }

    // ---------- gerar temas de redação (inalterado) ----------
    public List<String> gerarTemasRedacao() {
        // ... (lógica inalterada de temas de redação) ...
        String prompt = """
                Gere 3 temas de redação no modelo ENEM sobre atualidades.
                Retorne SOMENTE JSON: {"temas": ["tema 1", "tema 2", "tema 3"]}
                """;

        try {
            String resposta = gerarResposta(prompt);
            String jsonPuro = limparJson(resposta);
            JsonNode root = objectMapper.readTree(jsonPuro);
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

    // ---------- integração com a API Gemini (inalterado) ----------
    private String gerarResposta(String prompt) {
        // ... (lógica de chamada da API e retry) ...
        log.info("Enviando prompt: {}", prompt);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        final int MAX_RETRIES = 3;
        final long DELAY_MS = 1500;
        int attempts = 0;

        while (attempts < MAX_RETRIES) {
            attempts++;
            try {
                String raw = rest.post()
                        .uri("/models/" + model + ":generateContent?key=" + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .body(String.class);

                log.info("Resposta bruta da API Gemini: {}", raw);
                return extrairTexto(raw);

            } catch (RestClientException e) {
                boolean isServiceUnavailable = e.getMessage() != null && e.getMessage().contains("503 Service Unavailable");

                if (isServiceUnavailable && attempts < MAX_RETRIES) {
                    log.warn("Tentativa #{} falhou com 503 (Serviço Indisponível). Tentando novamente em {}ms...", attempts, DELAY_MS);
                    try {
                        Thread.sleep(DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupção durante o sleep.", ie);
                        throw new RuntimeException("Falha ao gerar conteúdo: Interrompido durante a espera.", e);
                    }
                } else {
                    log.error("Erro final ao chamar Gemini na tentativa #{}", attempts, e);
                    throw new RuntimeException("Falha ao gerar conteúdo", e);
                }
            }
        }

        throw new RuntimeException("Falha ao gerar conteúdo: Número máximo de tentativas (" + MAX_RETRIES + ") excedido após múltiplas falhas.");
    }


    private String extrairTexto(String raw) {
        // ... (lógica inalterada de extração de texto) ...
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

    // ---------- utilitários (inalterado) ----------

    /** Remove markdown e extrai apenas o conteúdo JSON válido */
    private String limparJson(String respostaBruta) {
        if (respostaBruta == null) return "";
        String limpa = respostaBruta
                .replaceAll("(?i)```json", "")
                .replaceAll("```", "")
                .trim();

        int inicio = limpa.indexOf("{");
        int fim = limpa.lastIndexOf("}") + 1;

        if (inicio >= 0 && fim > inicio) {
            limpa = limpa.substring(inicio, fim);
        }
        return limpa;
    }

    /** Gera uma resposta e converte diretamente para um objeto Java */
    private <T> T gerarJson(String prompt, Class<T> tipo) {
        String resposta = gerarResposta(prompt);
        String jsonLimpo = limparJson(resposta);
        try {
            return objectMapper.readValue(jsonLimpo, tipo);
        } catch (Exception e) {
            log.error("Erro ao converter JSON para " + tipo.getSimpleName(), e);
            throw new RuntimeException("Falha ao interpretar JSON da API Gemini", e);
        }
    }
}