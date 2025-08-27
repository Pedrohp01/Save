package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.RelatorioDesempenhoDTO;
import com.savemystudies.backend.model.QuestaoUsuario;
import com.savemystudies.backend.repository.QuestaoUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestaoUsuarioService {

    private final QuestaoUsuarioRepository questaoUsuarioRepository;

    public QuestaoUsuarioService(QuestaoUsuarioRepository questaoUsuarioRepository) {
        this.questaoUsuarioRepository = questaoUsuarioRepository;
    }

    public QuestaoUsuario salvarResposta(QuestaoUsuario questaoUsuario) {
        boolean acertou = questaoUsuario.getAlternativaSelecionada() != null &&
                questaoUsuario.getAlternativaSelecionada().equalsIgnoreCase(questaoUsuario.getCorreta());

        questaoUsuario.setAcertou(acertou);

        return questaoUsuarioRepository.save(questaoUsuario);
    }

    public List<QuestaoUsuario> listarPorUsuario(Long usuarioId) {
        return questaoUsuarioRepository.findByUsuarioId(usuarioId);
    }

    // Relatório detalhado
    public List<RelatorioDesempenhoDTO> desempenhoAgrupado(Long usuarioId, String agrupador) {
        List<QuestaoUsuario> respostas = questaoUsuarioRepository.findByUsuarioId(usuarioId);

        Map<String, List<QuestaoUsuario>> agrupado = respostas.stream()
                .collect(Collectors.groupingBy(r -> getCampo(r, agrupador)));

        return agrupado.entrySet().stream().map(entry -> {
            String chave = entry.getKey();
            List<QuestaoUsuario> lista = entry.getValue();

            long total = lista.size();
            long acertos = lista.stream().filter(QuestaoUsuario::isAcertou).count();
            double percentual = total > 0 ? (acertos * 100.0) / total : 0.0;

            return new RelatorioDesempenhoDTO(chave, total, acertos, percentual);
        }).toList();
    }

    private String getCampo(QuestaoUsuario r, String agrupador) {
        return switch (agrupador.toLowerCase()) {
            case "area" -> r.getArea();
            case "disciplina" -> r.getMateria();
            case "topico" -> r.getTopico();
            case "subtopico" -> r.getSubtopico();
            default -> "Outro";
        };
    }
}
