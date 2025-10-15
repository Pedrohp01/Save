package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.ExercicioGerado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExercicioGeradoRepository extends JpaRepository<ExercicioGerado, Long> {

}