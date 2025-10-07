package com.agendamento.salas.domain.exception;

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
// Vamos criar uma exceção customizada para os casos de validação que falharem. Isso vai ajudat a manter a lógica de negócio separada dos erros técnicos.