package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.RelatorioDesempenho;
import com.savemystudies.backend.model.Exercicio;
import com.savemystudies.backend.model.Redacao;
import com.savemystudies.backend.model.User;
import com.savemystudies.backend.repository.ExercicioRepository;
import com.savemystudies.backend.repository.RedacaoRepository;
import com.savemystudies.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesempenhoService {

    private final UserRepository userRepository;
    private final ExercicioRepository exercicioRepository;
    private final RedacaoRepository redacaoRepository;

    public DesempenhoService(UserRepository userRepository, ExercicioRepository exercicioRepository, RedacaoRepository redacaoRepository) {
        this.userRepository = userRepository;
        this.exercicioRepository = exercicioRepository;
        this.redacaoRepository = redacaoRepository;
    }

    public Exercicio salvarExercicio(Long usuarioId, Exercicio exercicio) {
        User usuario = userRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        exercicio.setUsuario(usuario);
        return exercicioRepository.save(exercicio);
    }

    public Redacao salvarRedacao(Long usuarioId, Redacao redacao) {
        User usuario = userRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        redacao.setUsuario(usuario);
        return redacaoRepository.save(redacao);
    }

    public RelatorioDesempenho gerarRelatorio(Long usuarioId) {
        User usuario = userRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        List<Exercicio> exercicios = exercicioRepository.findByUsuarioId(usuarioId);

        // CORRIGIDO: Agora chamamos o método com o nome correto 'findByUsuarioId'
        List<Redacao> redacoes = redacaoRepository.findByUsuarioId(usuarioId);

        double mediaExercicios = exercicios.stream()
                .mapToDouble(e -> e.getNota() != null ? e.getNota() : 0)
                .average().orElse(0);
        double mediaRedacoes = redacoes.stream()
                .mapToDouble(r -> r.getNota() != null ? r.getNota() : 0)
                .average().orElse(0);
        RelatorioDesempenho relatorio = new RelatorioDesempenho();
        relatorio.setNomeUsuario(usuario.getNome());
        relatorio.setMediaExercicios(mediaExercicios);
        relatorio.setMediaRedacoes(mediaRedacoes);
        relatorio.setExerciciosRecentes(exercicios);
        relatorio.setRedacoesRecentes(redacoes);
        return relatorio;
    }
}