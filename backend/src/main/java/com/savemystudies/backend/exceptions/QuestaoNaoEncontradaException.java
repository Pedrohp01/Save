package com.savemystudies.backend.exceptions;

// ⚠️ CORRIGIDO: Deve estender RuntimeException para ser uma Unchecked Exception
public class QuestaoNaoEncontradaException extends RuntimeException {

    // Construtor que aceita uma mensagem de erro (usado para logar ou mostrar detalhes)
    public QuestaoNaoEncontradaException(String message) {
        super(message);
    }

    // Opcional: Construtor que aceita a mensagem e a causa (stack trace)
    public QuestaoNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}