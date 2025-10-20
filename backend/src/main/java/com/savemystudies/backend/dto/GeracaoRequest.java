package com.savemystudies.backend.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public record GeracaoRequest(Long subtopicoId, String area, String materia, String topico, String subtopico) {}