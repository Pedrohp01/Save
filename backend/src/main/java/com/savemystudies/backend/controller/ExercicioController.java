package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.ExercicioResponse; // DTO de saída de geração
import com.savemystudies.backend.dto.FeedbackUnicoResponse; // DTO de saída de correção
import com.savemystudies.backend.dto.RespostaUnicaDTO; // DTO de entrada de correção
import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.service.CorrecaoService; // Novo Service
import com.savemystudies.backend.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final GeminiService geminiService;
    private final CorrecaoService correcaoService; // NOVO
    private final SubtopicoRepository subtopicoRepository;

    // 12. ENDPOINT DE GERAÇÃO: Retorna o JSON com os IDs do BD
    @GetMapping("/{areaId}/{materiaId}/{topicoId}/{subtopicoId}")
    public ResponseEntity<ExercicioResponse> gerarExercicio(
            @PathVariable Long areaId,
            @PathVariable Long materiaId,
            @PathVariable Long topicoId,
            @PathVariable Long subtopicoId) {

        return subtopicoRepository.findById(subtopicoId)
                .filter(subtopico ->
                        subtopico.getTopico().getMateria().getArea().getId().equals(areaId) &&
                                subtopico.getTopico().getMateria().getId().equals(materiaId) &&
                                subtopico.getTopico().getId().equals(topicoId)
                )
                .map(subtopico -> {
                    String area = subtopico.getTopico().getMateria().getArea().getNome();
                    String materia = subtopico.getTopico().getMateria().getNome();
                    String topico = subtopico.getTopico().getNome();
                    String nomeSubtopico = subtopico.getNome();

                    // Chama o serviço para gerar, salvar e retornar o DTO com os IDs
                    ExercicioResponse exercicio = geminiService.gerarExercicio(
                            subtopicoId, area, materia, topico, nomeSubtopico
                    );

                    return ResponseEntity.ok(exercicio);
                })
                .orElse(
                        // Retorna 400 Bad Request se a validação falhar
                        ResponseEntity.badRequest().<ExercicioResponse>build()
                );
    }

    // 13. ENDPOINT DE CORREÇÃO INDIVIDUAL: Retorna o feedback imediato
    @PostMapping("/corrigir/unica")
    public ResponseEntity<FeedbackUnicoResponse> corrigirRespostaUnica(@RequestBody RespostaUnicaDTO respostaUnica) {

        if (respostaUnica.questaoId() == null || respostaUnica.resposta() == null || respostaUnica.resposta().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Chama o serviço de correção
            FeedbackUnicoResponse feedback = correcaoService.corrigirQuestaoUnica(
                    respostaUnica.questaoId(),
                    respostaUnica.resposta()
            );
            return ResponseEntity.ok(feedback);

        } catch (RuntimeException e) {
            // Se a questão não for encontrada no BD, retorna 404
            return ResponseEntity.notFound().build();
        }
    }
}