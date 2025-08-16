// src/main/java/com/savemystudies/backend/controller/MenuController.java
package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Area;
import com.savemystudies.backend.model.Materia;
import com.savemystudies.backend.model.Subtopico;
import com.savemystudies.backend.model.Topico;
import com.savemystudies.backend.repository.AreaRepository;
import com.savemystudies.backend.repository.MateriaRepository;
import com.savemystudies.backend.repository.SubtopicoRepository;
import com.savemystudies.backend.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin; // <-- Adicione esta importação

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:8080") // <-- Adicione esta anotação
// Você pode usar @CrossOrigin(origins = "*") para permitir todas as origens temporariamente
public class MenuController {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private SubtopicoRepository subtopicoRepository;

    @GetMapping("/areas")
    public List<Area> getAreas() {
        return areaRepository.findAll();
    }

    // ... e o resto dos seus métodos
}