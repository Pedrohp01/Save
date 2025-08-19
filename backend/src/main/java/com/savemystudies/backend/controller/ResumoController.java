package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/resumo")
public class ResumoController {

    private final SubtopicoRepository subtopicoRepository;
    private final GeminiService geminiService;

    public ResumoController(SubtopicoRepository subtopicoRepository, GeminiService geminiService) {
        this.subtopicoRepository = subtopicoRepository;
        this.geminiService = geminiService;
    }

    /**
     * Gera resumo de um sub-tópico específico
     * Exemplo: GET /api/resumo/15
     */
    @GetMapping("/{subtopicoId}")
    public Mono<ResponseEntity<String>> generateResumo(@PathVariable Long subtopicoId) {
        return Mono.justOrEmpty(subtopicoRepository.findById(subtopicoId))
                .flatMap(subtopico -> {
                    String area = subtopico.getTopico().getMateria().getArea().getNome();
                    String materia = subtopico.getTopico().getMateria().getNome();
                    String topico = subtopico.getTopico().getNome();

                    return geminiService.gerarResumo(area, materia, topico)
                            .map(ResponseEntity::ok);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Gera resumo completo passando toda a hierarquia: Área -> Matéria -> Tópico -> SubTópico
     * Exemplo: GET /api/resumo/completo/1/3/7/15
     */
    @GetMapping("/completo/{areaId}/{materiaId}/{topicoId}/{subtopicoId}")
    public Mono<ResponseEntity<String>> generateResumoCompleto(
            @PathVariable Long areaId,
            @PathVariable Long materiaId,
            @PathVariable Long topicoId,
            @PathVariable Long subtopicoId) {

        return Mono.justOrEmpty(subtopicoRepository.findById(subtopicoId))
                .flatMap(subtopico -> {
                    // Aqui você pode adicionar validações extras se quiser garantir
                    // que o SubTópico pertence ao Tópico -> Matéria -> Área
                    String area = subtopico.getTopico().getMateria().getArea().getNome();
                    String materia = subtopico.getTopico().getMateria().getNome();
                    String topico = subtopico.getTopico().getNome();

                    return geminiService.gerarResumo(area, materia, topico)
                            .map(ResponseEntity::ok);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
