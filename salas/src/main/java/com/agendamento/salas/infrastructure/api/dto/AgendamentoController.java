package com.agendamento.salas.infrastructure.api.dto;

import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.application.usecase.impl.CriarAgendamentoUseCaseImpl;
import com.agendamento.salas.domain.entity.Agendamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// essa anotação marca a classe como um Controller REST
@RestController
@RequestMapping("/v1/agendamentos") // Define o caminho base da API
public class AgendamentoController {

    private final CriarAgendamentoUseCaseImpl criarAgendamentoUseCase;

    // Injeção da Porta de Entrada (o Use Case)
    public AgendamentoController(CriarAgendamentoUseCaseImpl criarAgendamentoUseCase) {
        this.criarAgendamentoUseCase = criarAgendamentoUseCase;
    }

    // Endpoint para criar um novo agendamento
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@RequestBody CriarAgendamentoRequest request) {

        // O Controller chama o Use Case, que orquestra a lógica
        Agendamento agendamentoSalvo = criarAgendamentoUseCase.execute(request);

        // Mapeia a Entidade de Negócio para o DTO de Resposta
        AgendamentoResponse response = AgendamentoResponse.fromDomain(agendamentoSalvo);

        // Retorna a resposta HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}