package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    List<Topico> findByMateriaId(Long materiaId);
}
