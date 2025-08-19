package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Topico;
import com.savemystudies.backend.repository.TopicoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    /**
     * Retorna todos os tópicos de uma matéria específica
     * Exemplo: GET /topicos/materia/1
     */
    @GetMapping("/materia/{materiaId}")
    public List<Topico> listarPorMateria(@PathVariable Long materiaId) {
        return topicoRepository.findByMateriaId(materiaId);
    }

    /**
     * Retorna todos os tópicos
     * Exemplo: GET /topicos
     */
    @GetMapping
    public List<Topico> listarTodos() {
        return topicoRepository.findAll();
    }
}
