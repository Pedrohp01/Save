 package com.savemystudies.backend.dto;

import java.util.List;

public record RelatorioResponse(
        Double desempenhoGeral, // Percentual de acerto total
        List<DesempenhoDetalhe> porMateria,
        List<DesempenhoDetalhe> porTopico
        // Pode adicionar por Subtopico
) {}