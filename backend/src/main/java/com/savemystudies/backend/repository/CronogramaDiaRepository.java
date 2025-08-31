package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.CronogramaDia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronogramaDiaRepository extends JpaRepository<CronogramaDia, Long> {
    List<CronogramaDia> findByCronogramaId(Long cronogramaId);
}
