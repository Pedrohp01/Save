package com.savemystudies.backend.service;

import com.savemystudies.backend.model.Cronograma;
import com.savemystudies.backend.repository.CronogramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CronogramaService {

    private final CronogramaRepository cronogramaRepository;

    @Autowired
    public CronogramaService(CronogramaRepository cronogramaRepository) {
        this.cronogramaRepository = cronogramaRepository;
    }

    public List<Cronograma> buscarTodos() {
        return cronogramaRepository.findAll();
    }

    public Cronograma buscarPorId(Long id) {
        return cronogramaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma não encontrado com o ID: " + id));
    }

    public Cronograma editar(Long id, Cronograma cronogramaAtualizado) {
        // Encontra o cronograma existente
        return cronogramaRepository.findById(id)
                .map(cronogramaExistente -> {
                    // Atualiza os campos necessários
                    cronogramaExistente.setVestibular(cronogramaAtualizado.getVestibular());
                    cronogramaExistente.setDiasDaSemana(cronogramaAtualizado.getDiasDaSemana());
                    cronogramaExistente.setMinutospordia(cronogramaAtualizado.getMinutospordia());
                    cronogramaExistente.setDataInicio(cronogramaAtualizado.getDataInicio());
                    cronogramaExistente.setDataFim(cronogramaAtualizado.getDataFim());
                    // Salva e retorna o cronograma editado
                    return cronogramaRepository.save(cronogramaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Cronograma não encontrado com o ID: " + id));
    }

    public void deletar(Long id) {
        if (!cronogramaRepository.existsById(id)) {
            throw new RuntimeException("Cronograma não encontrado com o ID: " + id);
        }
        cronogramaRepository.deleteById(id);
    }
}