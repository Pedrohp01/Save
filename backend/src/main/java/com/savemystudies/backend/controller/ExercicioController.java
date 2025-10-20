package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.ExercicioResponse;
import com.savemystudies.backend.dto.FeedbackUnicoResponse;
import com.savemystudies.backend.dto.ItemCurriculoDTO;
import com.savemystudies.backend.dto.RespostaUnicaDTO;
import com.savemystudies.backend.exceptions.QuestaoNaoEncontradaException;
import com.savemystudies.backend.exceptions.QuestaoNaoEncontradaException;
import com.savemystudies.backend.repository.*;
import com.savemystudies.backend.service.CorrecaoService;
import com.savemystudies.backend.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercicios") // Prefixo para Exercícios
@RequiredArgsConstructor
public class ExercicioController {

    // Injeções para Navegação (Duplicadas por Requisito do Front-end)
    private final AreaRepository areaRepository;
    private final MateriaRepository materiaRepository;
    private final TopicoRepository topicoRepository;
    private final SubtopicoRepository subtopicoRepository;

    // Injeções para Lógica de Exercício
    private final GeminiService geminiService;
    private final CorrecaoService correcaoService;

    // ===========================================
    // MÉTODOS DE NAVEGAÇÃO (GETs)
    // ===========================================

    // LISTA DE ÁREAS -> GET /api/exercicios/areas
    @GetMapping("/areas")
    public List<ItemCurriculoDTO> listarAreas() {
        return areaRepository.findAll().stream()
                .map(a -> new ItemCurriculoDTO(a.getId(), a.getNome()))
                .collect(Collectors.toList());
    }

    // LISTA DE MATÉRIAS POR ÁREA -> GET /api/exercicios/areas/{areaId}/materias
    @GetMapping("/areas/{areaId}/materias")
    public List<ItemCurriculoDTO> listarMaterias(@PathVariable Long areaId) {
        return materiaRepository.findByAreaId(areaId).stream()
                .map(m -> new ItemCurriculoDTO(m.getId(), m.getNome()))
                .collect(Collectors.toList());
    }

    // LISTA DE TÓPICOS POR MATÉRIA -> GET /api/exercicios/materias/{materiaId}/topicos
    @GetMapping("/materias/{materiaId}/topicos")
    public List<ItemCurriculoDTO> listarTopicos(@PathVariable Long materiaId) {
        return topicoRepository.findByMateriaId(materiaId).stream()
                .map(t -> new ItemCurriculoDTO(t.getId(), t.getNome()))
                .collect(Collectors.toList());
    }

    // LISTA DE SUBTÓPICOS POR TÓPICO -> GET /api/exercicios/topicos/{topicoId}/subtopicos
    @GetMapping("/topicos/{topicoId}/subtopicos")
    public List<ItemCurriculoDTO> listarSubtopicos(@PathVariable Long topicoId) {
        return subtopicoRepository.findByTopicoId(topicoId).stream()
                .map(s -> new ItemCurriculoDTO(s.getId(), s.getNome()))
                .collect(Collectors.toList());
    }

    // ===========================================
    // GERAÇÃO DE EXERCÍCIO (POST)
    // ===========================================

    // URL: /api/exercicios/gerar/{areaId}/{materiaId}/{topicoId}/{subtopicoId}
    @PostMapping("/{areaId}/{materiaId}/{topicoId}/{subtopicoId}/gerar")
    public ResponseEntity<ExercicioResponse> gerarExercicio(
            @PathVariable Long areaId,
            @PathVariable Long materiaId,
            @PathVariable Long topicoId,
            @PathVariable Long subtopicoId){
        return subtopicoRepository.findById(subtopicoId)
                .filter(subtopico ->
                        subtopico.getTopico().getMateria().getArea().getId().equals(areaId) &&
                                subtopico.getTopico().getMateria().getId().equals(materiaId) &&
                                subtopico.getTopico().getId().equals(topicoId)
                )
                .map(subtopico -> {
                    // 1. Obtém todos os dados, incluindo o ID (Correção do Erro da Imagem)
                    Long subtopicoIdParaService = subtopico.getId();
                    String area = subtopico.getTopico().getMateria().getArea().getNome();
                    String materia = subtopico.getTopico().getMateria().getNome();
                    String topico = subtopico.getTopico().getNome();
                    String nomeSubtopico = subtopico.getNome();

                    // 2. Chama o Service com os 5 argumentos
                    ExercicioResponse exercicio = geminiService.gerarExercicio(
                            subtopicoIdParaService, // O argumento Long Faltante
                            area,
                            materia,
                            topico,
                            nomeSubtopico
                    );
                    return ResponseEntity.ok(exercicio);
                })
                .orElse(
                        // Retorna 400 Bad Request se a validação falhar
                        ResponseEntity.badRequest().<ExercicioResponse>build()
                );
    }

    // ===========================================
    // CORREÇÃO DA RESPOSTA ÚNICA (POST)
    // ===========================================

    // URL: /api/exercicios/corrigir/unica
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

        } catch (com.savemystudies.backend.exceptions.QuestaoNaoEncontradaException e) {
            // Retorna 404 se o ID da questão não existir (Exceção lançada pelo Service)
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/areas/{areaId}/materias/{materiaId}/topicos/{topicoId}/subtopicos/{subtopicoId}")
    public List<ItemCurriculoDTO> listarSubtopicosComContexto(
            @PathVariable Long areaId,
            @PathVariable Long materiaId,
            @PathVariable Long topicoId) {

        // 1. Otimização: Busca o Tópico pelo ID principal
        return topicoRepository.findById(topicoId)
                // 2. Validação: Checa se os IDs de Matéria e Área na URL correspondem ao Tópico encontrado
                .filter(topico ->
                        topico.getMateria().getId().equals(materiaId) &&
                                topico.getMateria().getArea().getId().equals(areaId)
                )
                .map(topico -> {
                    // 3. Se a validação passar, lista os Subtópicos daquele Tópico
                    return subtopicoRepository.findByTopicoId(topicoId).stream()
                            .map(s -> new ItemCurriculoDTO(s.getId(), s.getNome()))
                            .collect(Collectors.toList());
                })
                // 4. Se o Tópico não for encontrado ou a validação falhar, retorna uma lista vazia
                .orElse(Collections.emptyList());
    }
}