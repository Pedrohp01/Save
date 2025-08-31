package com.savemystudies.backend.repository;

import com.savemystudies.backend.model.Redacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedacaoRepository extends JpaRepository<Redacao, Long> {

    // Correto: use o nome da propriedade de relacionamento 'usuario'
    // e o nome da propriedade de ID 'id'.
    List<Redacao> findByUsuarioId(Long usuarioId);
}