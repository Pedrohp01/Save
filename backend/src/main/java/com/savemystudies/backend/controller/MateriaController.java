package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Materia;
import com.savemystudies.backend.repository.MateriaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias")
public class MateriaController {

    private final MateriaRepository materiaRepository;

    public MateriaController(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }
    /**
     * Retorna todas as matérias vinculadas a uma área específica
     * Exemplo: GET /materias/area/1
     */
    @GetMapping("/area/{areaId}")
    public List<Materia> listarPorArea(@PathVariable Long areaId) {
        return materiaRepository.findByArea_Id(areaId);
    }
}
