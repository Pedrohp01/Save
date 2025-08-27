package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Cronograma;
import com.savemystudies.backend.service.CronogramaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cronogramas")
public class CronogramaController {

    private final CronogramaService cronogramaService;

    public CronogramaController(CronogramaService cronogramaService) {
        this.cronogramaService = cronogramaService;
    }

    @PostMapping
    public ResponseEntity<Cronograma> criar(@RequestBody Cronograma cronograma) {
        return ResponseEntity.ok(cronogramaService.salvar(cronograma));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cronograma> buscar(@PathVariable Long id) {
        Optional<Cronograma> cronograma = cronogramaService.buscarPorId(id);
        return cronograma.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/atividade/{atividadeId}/concluir")
    public ResponseEntity<Void> concluirAtividade(@PathVariable Long atividadeId) {
        cronogramaService.concluirAtividade(atividadeId);
        return ResponseEntity.ok().build();
    }
}
