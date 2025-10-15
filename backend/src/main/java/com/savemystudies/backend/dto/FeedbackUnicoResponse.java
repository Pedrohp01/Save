package com.savemystudies.backend.dto;

// DTO para enviar o resultado da correção imediata ao cliente
public record FeedbackUnicoResponse(
        boolean correta,
        String respostaCorreta,
        String explicacao
) {}