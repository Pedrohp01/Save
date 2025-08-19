package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.repository.SubtopicoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subtopicos")
public class SubtopicoController {

    private final SubtopicoRepository subtopicoRepository;

    public SubtopicoController(SubtopicoRepository subtopicoRepository) {
        this.subtopicoRepository = subtopicoRepository;
    }

    /**
     * Retorna todos os sub-tópicos
     * Exemplo: GET /subtopicos
     */
    @GetMapping
    public List<Subtopico> listarTodos() {
        return subtopicoRepository.findAll();
    }

    /**
     * Retorna todos os sub-tópicos de um tópico específico
     * Exemplo: GET /subtopicos/topico/7
     */
    @GetMapping("/topico/{topicoId}")
    public List<Subtopico> listarPorTopico(@PathVariable Long topicoId) {
        return subtopicoRepository.findByTopicoId(topicoId);
    }
}
