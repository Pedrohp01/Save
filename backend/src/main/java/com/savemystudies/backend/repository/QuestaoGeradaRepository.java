package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.QuestaoGerada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoGeradaRepository extends JpaRepository<QuestaoGerada, Long> {

}