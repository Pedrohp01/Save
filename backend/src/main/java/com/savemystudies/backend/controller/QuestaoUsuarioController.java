package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.RelatorioDesempenhoDTO;
import com.savemystudies.backend.model.QuestaoUsuario;
import com.savemystudies.backend.service.QuestaoUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questoes-usuario")
public class QuestaoUsuarioController {

    private final QuestaoUsuarioService questaoUsuarioService;

    public QuestaoUsuarioController(QuestaoUsuarioService questaoUsuarioService) {
        this.questaoUsuarioService = questaoUsuarioService;
    }

    @PostMapping
    public ResponseEntity<QuestaoUsuario> salvarResposta(@RequestBody QuestaoUsuario questaoUsuario) {
        return ResponseEntity.ok(questaoUsuarioService.salvarResposta(questaoUsuario));
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<QuestaoUsuario>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(questaoUsuarioService.listarPorUsuario(usuarioId));
    }

    // Relatório detalhado (área, disciplina, tópico ou subtopico)
    @GetMapping("/{usuarioId}/desempenho/{nivel}")
    public ResponseEntity<List<RelatorioDesempenhoDTO>> desempenhoAgrupado(
            @PathVariable Long usuarioId,
            @PathVariable String nivel) {

        return ResponseEntity.ok(questaoUsuarioService.desempenhoAgrupado(usuarioId, nivel));
    }
}
