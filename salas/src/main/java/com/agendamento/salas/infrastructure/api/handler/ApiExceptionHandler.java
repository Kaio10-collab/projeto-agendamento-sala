package com.agendamento.salas.infrastructure.api.handler;

import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice // Indica que esta classe lida com exceções de todos os Controllers
public class ApiExceptionHandler {

    // Trata a nossa exceção de negócio
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, String>> handleRegraDeNegocioException(RegraDeNegocioException ex) {

        // Retorna o status HTTP 400 Bad Request
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", "Regra de Negócio Violada", "mensagem", ex.getMessage()));
    }
}
