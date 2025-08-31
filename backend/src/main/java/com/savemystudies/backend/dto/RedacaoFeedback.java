package com.savemystudies.backend.dto;// com.savemystudies.backend.dto.RedacaoFeedback
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedacaoFeedback {
    private String feedback;
    private double nota;
}