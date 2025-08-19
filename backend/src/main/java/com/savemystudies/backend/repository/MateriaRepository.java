package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByArea_Id(Long areaId);
}
