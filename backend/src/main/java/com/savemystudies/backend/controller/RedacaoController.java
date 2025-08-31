package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Redacao;
import com.savemystudies.backend.service.RedacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/redacoes")
public class RedacaoController {

    private final RedacaoService redacaoService;

    @Autowired
    public RedacaoController(RedacaoService redacaoService) {
        this.redacaoService = redacaoService;
    }

    // Endpoint 1: Retorna a lista de temas pré-prontos
    @GetMapping("/temas-prontos")
    public ResponseEntity<List<String>> getTemasProntos() {
        List<String> temas = redacaoService.getTemasProntos();
        return ResponseEntity.ok(temas);
    }

    // Endpoint 2: Gera novos temas usando o Gemini
    @GetMapping("/temas-gerar")
    public ResponseEntity<List<String>> gerarTemas() {
        List<String> temas = redacaoService.gerarTemas();
        return ResponseEntity.ok(temas);
    }


    @PostMapping("/feedback/{userId}")
    public ResponseEntity<Redacao> gerarFeedbackESalvar(@PathVariable Long userId, @RequestBody Redacao redacao) {
        Redacao redacaoSalva = redacaoService.gerarFeedbackESalvar(redacao, userId);
        return new ResponseEntity<>(redacaoSalva, HttpStatus.CREATED);
    }
}