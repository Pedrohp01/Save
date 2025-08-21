package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.QuestaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestaoUsuarioRepository extends JpaRepository<QuestaoUsuario, Long> {

    // Buscar todas as questões respondidas por um usuário específico
    List<QuestaoUsuario> findByUsuarioId(Long usuarioId);

    // Buscar questões respondidas por usuário e por área
    List<QuestaoUsuario> findByUsuarioIdAndArea(Long usuarioId, String area);

    // Buscar questões respondidas por usuário e matéria
    List<QuestaoUsuario> findByUsuarioIdAndMateria(Long usuarioId, String materia);

    // Buscar questões por usuário e tópico
    List<QuestaoUsuario> findByUsuarioIdAndTopico(Long usuarioId, String topico);

    // Buscar questões por usuário e subtópico
    List<QuestaoUsuario> findByUsuarioIdAndSubtopico(Long usuarioId, String subtopico);
}
