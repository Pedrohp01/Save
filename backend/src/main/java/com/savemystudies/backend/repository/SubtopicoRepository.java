// src/main/java/com/savemystudies.backend.repository.SubtopicoRepository.java

package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Subtopico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicoRepository extends JpaRepository<Subtopico, Long> {

    // The correct method name is findByTopicoId
    List<Subtopico> findByTopicoId(Long topicoId);
}