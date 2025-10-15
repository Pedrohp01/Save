// src/main/java/com/savemystudies/backend/dto/ExercicioResponse.java

package com.savemystudies.backend.dto;

// REMOVA: import com.savemystudies.backend.model.Questao;
// O tipo Questao está no mesmo pacote, então não precisa ser importado.
import java.util.List;

public record ExercicioResponse(
        // Agora o compilador entende que Questao é o record 'Questao.java'
        List<Questao> questoes
) {}