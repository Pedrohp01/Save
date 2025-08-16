// src/main/java/com/savemystudies/backend/controller/ExercicioController.java
package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/exercicio")
public class ExercicioController {

    @Autowired
    private SubtopicoRepository subtopicoRepository;

    @Autowired
    private GeminiService geminiService;

    @GetMapping
    public Mono<ResponseEntity<String>> generateExercicio(@RequestParam Long subtopicoId) {
        Optional<Subtopico> optionalSubtopico = subtopicoRepository.findById(subtopicoId);

        if (optionalSubtopico.isEmpty()) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        String subtopicoNome = optionalSubtopico.get().getNome();

        // Novo prompt para gerar a questão completa
        String prompt = String.format(
                "Crie uma questão de múltipla escolha em português, no formato ENEM, sobre o tópico '%s'. " +
                        "A questão deve ter um enunciado claro e cinco alternativas (A, B, C, D, E). " +
                        "No final, indique a alternativa correta e forneça uma explicação detalhada sobre o porquê da resposta estar correta e as demais estarem incorretas. " +
                        "Formate a resposta da seguinte maneira: \n\n" +
                        "**Enunciado:** [Seu texto aqui]\n\n" +
                        "**Alternativas:**\n" +
                        "A) [Alternativa A]\n" +
                        "B) [Alternativa B]\n" +
                        "C) [Alternativa C]\n" +
                        "D) [Alternativa D]\n" +
                        "E) [Alternativa E]\n\n" +
                        "**Resposta Correta:** [Letra da resposta, ex: C]\n\n" +
                        "**Explicação:** [Seu texto de explicação aqui]",
                subtopicoNome
        );

        return geminiService.gerarResposta(prompt)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}