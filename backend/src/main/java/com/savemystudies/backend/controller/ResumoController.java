package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Subtopico; // Importação correta
import com.savemystudies.backend.repository.SubtopicoRepository; // Repositório correto
import com.savemystudies.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/resumo")
public class ResumoController {

    @Autowired
    private SubtopicoRepository subtopicoRepository; // Injetando o repositório correto

    @Autowired
    private GeminiService geminiService;

    @GetMapping
    public Mono<ResponseEntity<String>> generateResumo(@RequestParam Long subtopicoId) {
        Optional<Subtopico> optionalSubtopico = subtopicoRepository.findById(subtopicoId);

        if (optionalSubtopico.isEmpty()) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        String subtopicoNome = optionalSubtopico.get().getNome();
        String prompt = "Gere um resumo detalhado e claro sobre o seguinte tema para fins de estudo: " + subtopicoNome;

        return geminiService.gerarResposta(prompt)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}