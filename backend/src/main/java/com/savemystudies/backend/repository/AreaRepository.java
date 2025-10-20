package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    // Métodos de busca padrão são herdados (findAll, findById)
}