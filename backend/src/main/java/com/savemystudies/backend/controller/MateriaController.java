package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Materia;
import com.savemystudies.backend.repository.MateriaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/materias")
public class MateriaController {

    private final MateriaRepository materiaRepository;

    public MateriaController(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    // Buscar todas as matérias
    @GetMapping
    public List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    // Buscar matérias por área
    @GetMapping("/area/{areaId}")
    public List<Materia> listarPorArea(@PathVariable Long areaId) {
        return materiaRepository.findByAreaId(areaId);
    }
}
