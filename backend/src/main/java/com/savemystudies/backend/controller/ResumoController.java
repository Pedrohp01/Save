
package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumos")
@RequiredArgsConstructor
public class ResumoController {

    private final GeminiService geminiService;
    private final SubtopicoRepository subtopicoRepository;

    @GetMapping("/{areaId}/{materiaId}/{topicoId}/{subtopicoId}")
    public ResponseEntity<String> gerarResumo(
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

                    String resumo = geminiService.gerarResumo(area, materia, topico, nomeSubtopico);

                    return ResponseEntity.ok(resumo);
                })
                .orElse(ResponseEntity.badRequest().body("Subtópico não encontrado ou IDs não correspondem"));
    }
}

