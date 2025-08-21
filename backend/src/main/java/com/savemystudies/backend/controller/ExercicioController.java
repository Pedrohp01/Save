package com.savemystudies.backend.controller;

import com.savemystudies.backend.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final GeminiService geminiService;

    public ExercicioController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Endpoint para gerar questões
    @GetMapping("/gerar")
    public String gerarExercicio(
            @RequestParam String area,
            @RequestParam String materia,
            @RequestParam String topico,
            @RequestParam String subtopico
    ) {
        return geminiService.gerarExercicio(area, materia, topico, subtopico);
    }

    // Endpoint para gerar resumo
    @GetMapping("/resumo")
    public String gerarResumo(
            @RequestParam String area,
            @RequestParam String materia,
            @RequestParam String topico,
            @RequestParam String subtopico
    ) {
        return geminiService.gerarResumo(area, materia, topico, subtopico);
    }
}
