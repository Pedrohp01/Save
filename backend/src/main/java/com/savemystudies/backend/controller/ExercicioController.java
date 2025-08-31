package com.savemystudies.backend.controller;

import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final GeminiService geminiService;
    private final SubtopicoRepository subtopicoRepository;

    // Endpoint para gerar questões (agora usando IDs)
    @GetMapping("/{areaId}/{materiaId}/{topicoId}/{subtopicoId}")
    public ResponseEntity<String> gerarExercicio(
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

                    String exercicio = geminiService.gerarExercicio(area, materia, topico, nomeSubtopico);

                    return ResponseEntity.ok(exercicio);
                })
                .orElse(ResponseEntity.badRequest().body("Subtópico não encontrado ou IDs não correspondem"));
    }


}