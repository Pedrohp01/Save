package com.savemystudies.backend.service;

import com.savemystudies.backend.model.*;
import com.savemystudies.backend.repository.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CronogramaService {

    private final CronogramaRepository cronogramaRepository;
    private final CronogramaAtividadeRepository atividadeRepository;

    public CronogramaService(CronogramaRepository cronogramaRepository,
                             CronogramaAtividadeRepository atividadeRepository) {
        this.cronogramaRepository = cronogramaRepository;
        this.atividadeRepository = atividadeRepository;
    }

    public Cronograma salvar(Cronograma cronograma) {
        return cronogramaRepository.save(cronograma);
    }

    public Optional<Cronograma> buscarPorId(Long id) {
        return cronogramaRepository.findById(id);
    }

    public void concluirAtividade(Long atividadeId) {
        atividadeRepository.findById(atividadeId).ifPresent(atividade -> {
            atividade.setConcluida(true);
            atividadeRepository.save(atividade);
        });
    }
}
