package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.ItemCurriculoDTO;
import com.savemystudies.backend.dto.ResumoResponse;
import com.savemystudies.backend.model.Subtopico; // Necessário para o .filter
import com.savemystudies.backend.repository.*;
import com.savemystudies.backend.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resumo") // Prefixo para Navegação e Resumos
@RequiredArgsConstructor
public class ResumoController {

    // Injeções de Repositórios para a navegação
    private final AreaRepository areaRepository;
    private final MateriaRepository materiaRepository;
    private final TopicoRepository topicoRepository;
    private final SubtopicoRepository subtopicoRepository;
    private final GeminiService geminiService; // Injeção do Service para o resumo

    // ===========================================
    // MÉTODOS DE NAVEGAÇÃO (GETs)
    // ===========================================

    /** Lista todas as Áreas. URL: GET /api/curriculo/areas */
    @GetMapping("/areas")
    public List<ItemCurriculoDTO> listarAreas() {
        return areaRepository.findAll().stream()
                .map(a -> new ItemCurriculoDTO(a.getId(), a.getNome()))
                .collect(Collectors.toList());
    }

    /** Lista Matérias por Área. URL: GET /api/curriculo/areas/{areaId}/materias */
    @GetMapping("/areas/{areaId}/materias")
    public List<ItemCurriculoDTO> listarMaterias(@PathVariable Long areaId) {
        return materiaRepository.findByAreaId(areaId).stream()
                .map(m -> new ItemCurriculoDTO(m.getId(), m.getNome()))
                .collect(Collectors.toList());
    }

    /** Lista Tópicos por Matéria. URL: GET /api/curriculo/materias/{materiaId}/topicos */
    @GetMapping("/materias/{materiaId}/topicos")
    public List<ItemCurriculoDTO> listarTopicos(@PathVariable Long materiaId) {
        return topicoRepository.findByMateriaId(materiaId).stream()
                .map(t -> new ItemCurriculoDTO(t.getId(), t.getNome()))
                .collect(Collectors.toList());
    }

    /** Lista Subtópicos por Tópico. URL: GET /api/curriculo/topicos/{topicoId}/subtopicos */
    @GetMapping("/topicos/{topicoId}/subtopicos")
    public List<ItemCurriculoDTO> listarSubtopicos(@PathVariable Long topicoId) {
        return subtopicoRepository.findByTopicoId(topicoId).stream()
                .map(s -> new ItemCurriculoDTO(s.getId(), s.getNome()))
                .collect(Collectors.toList());
    }

    // ===========================================
    // GERAÇÃO DE RESUMO
    // ===========================================

    /** * Gera um resumo.
     * URL: GET /api/curriculo/{areaId}/{materiaId}/{topicoId}/{subtopicoId}/resumo
     */
    @GetMapping("/{areaId}/{materiaId}/{topicoId}/{subtopicoId}/resumo")
    public ResponseEntity<ResumoResponse> gerarResumo(
            @PathVariable Long areaId,
            @PathVariable Long materiaId,
            @PathVariable Long topicoId,
            @PathVariable Long subtopicoId) {

        // 1. Busca o Subtópico pelo ID e VALIDA se ele pertence à hierarquia completa
        return subtopicoRepository.findById(subtopicoId)
                .filter(subtopico ->
                        subtopico.getTopico().getMateria().getArea().getId().equals(areaId) &&
                                subtopico.getTopico().getMateria().getId().equals(materiaId) &&
                                subtopico.getTopico().getId().equals(topicoId)
                )
                .map(subtopico -> {
                    // 2. Extrai nomes para o prompt da IA
                    String area = subtopico.getTopico().getMateria().getArea().getNome();
                    String materia = subtopico.getTopico().getMateria().getNome();
                    String topico = subtopico.getTopico().getNome();
                    String nomeSubtopico = subtopico.getNome();

                    // 3. Chama o serviço para gerar o resumo (retorna ResumoResponse)
                    ResumoResponse resumo = geminiService.gerarResumo(area, materia, topico, nomeSubtopico);
                    return ResponseEntity.ok(resumo);
                })
                .orElse(
                        // Retorna 400 Bad Request se a validação falhar ou o Subtópico não for encontrado
                        ResponseEntity.badRequest().<ResumoResponse>build()
                );
    }
}