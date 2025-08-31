package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    List<Exercicio> findByUsuarioId(Long usuarioId);
}