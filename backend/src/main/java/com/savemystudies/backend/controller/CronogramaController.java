package com.savemystudies.backend.controller;

import com.savemystudies.backend.model.Cronograma;
import com.savemystudies.backend.service.CronogramaService;
import com.savemystudies.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cronogramas")
public class CronogramaController {

    private final GeminiService geminiService;
    private final CronogramaService cronogramaService; // Novo serviço para lógica de CRUD

    @Autowired
    public CronogramaController(GeminiService geminiService, CronogramaService cronogramaService) {
        this.geminiService = geminiService;
        this.cronogramaService = cronogramaService;
    }

    // Endpoint 1: Criar um cronograma (POST)
    @PostMapping
    public ResponseEntity<Cronograma> criarCronograma(@RequestBody Cronograma cronograma) {
        // A lógica de criação com o Gemini fica no serviço
        Cronograma cronogramaGerado = geminiService.gerarCronograma(cronograma);
        return new ResponseEntity<>(cronogramaGerado, HttpStatus.CREATED);
    }

    // Endpoint 2: Buscar todos os cronogramas (GET)
    @GetMapping
    public ResponseEntity<List<Cronograma>> listarTodosCronogramas() {
        List<Cronograma> cronogramas = cronogramaService.buscarTodos();
        return ResponseEntity.ok(cronogramas);
    }

    // Endpoint 3: Buscar um cronograma por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Cronograma> buscarCronogramaPorId(@PathVariable Long id) {
        Cronograma cronograma = cronogramaService.buscarPorId(id);
        // O CronogramaService deve lançar uma exceção se não encontrar o ID,
        // e o Spring lida com a resposta 404 Not Found
        return ResponseEntity.ok(cronograma);
    }

    // Endpoint 4: Editar um cronograma (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Cronograma> editarCronograma(@PathVariable Long id, @RequestBody Cronograma cronogramaAtualizado) {
        Cronograma cronogramaEditado = cronogramaService.editar(id, cronogramaAtualizado);
        return ResponseEntity.ok(cronogramaEditado);
    }

    // Endpoint 5: Deletar um cronograma (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCronograma(@PathVariable Long id) {
        cronogramaService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para indicar sucesso sem corpo
    }
}