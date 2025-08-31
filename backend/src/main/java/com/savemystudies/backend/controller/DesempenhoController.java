package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.RelatorioDesempenho;
import com.savemystudies.backend.model.Exercicio;
import com.savemystudies.backend.model.Redacao;
import com.savemystudies.backend.service.DesempenhoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/desempenho")
public class DesempenhoController {

    private final DesempenhoService desempenhoService;

    public DesempenhoController(DesempenhoService desempenhoService) {
        this.desempenhoService = desempenhoService;
    }

    @PostMapping("/salvar-exercicio/{usuarioId}")
    public ResponseEntity<Exercicio> salvarExercicio(@PathVariable Long usuarioId, @Valid @RequestBody Exercicio exercicio) {
        Exercicio novoExercicio = desempenhoService.salvarExercicio(usuarioId, exercicio);
        return new ResponseEntity<>(novoExercicio, HttpStatus.CREATED);
    }

    @PostMapping("/salvar-redacao/{usuarioId}")
    public ResponseEntity<Redacao> salvarRedacao(@PathVariable Long usuarioId, @Valid @RequestBody Redacao redacao) {
        Redacao novaRedacao = desempenhoService.salvarRedacao(usuarioId, redacao);
        return new ResponseEntity<>(novaRedacao, HttpStatus.CREATED);
    }

    @GetMapping("/relatorio/{usuarioId}")
    public ResponseEntity<RelatorioDesempenho> gerarRelatorio(@PathVariable Long usuarioId) {
        RelatorioDesempenho relatorio = desempenhoService.gerarRelatorio(usuarioId);
        return ResponseEntity.ok(relatorio);
    }
}