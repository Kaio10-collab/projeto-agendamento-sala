package com.agendamento.salas.infrastructure.api;

import com.agendamento.salas.application.usecase.BuscarAgendamentosPorPessoaMapper;
import com.agendamento.salas.application.usecase.CriarAgendamentoMapper;
import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.application.usecase.impl.BuscarAgendamentosPorPessoaUseCaseImpl;
import com.agendamento.salas.application.usecase.impl.CriarAgendamentoUseCaseImpl;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.infrastructure.api.dto.AgendamentoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// essa anotação marca a classe como um Controller REST
@RestController
@RequestMapping("/v1/agendamentos") // Define o caminho base da API
public class AgendamentoController {

    private final CriarAgendamentoMapper criarAgendamentoUseCase;
    private final BuscarAgendamentosPorPessoaMapper buscarAgendamentosUseCase;

    // Injeção da Porta de Entrada (o Use Case)
    public AgendamentoController(
            CriarAgendamentoMapper criarAgendamentoUseCase,
            BuscarAgendamentosPorPessoaMapper buscarAgendamentosUseCase) {
        this.criarAgendamentoUseCase = criarAgendamentoUseCase;
        this.buscarAgendamentosUseCase = buscarAgendamentosUseCase;
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

    // Endpoint para buscar agendamentos por nome e sobrenome
    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> buscarAgendamentosPorPessoa(
            @RequestParam String nome,
            @RequestParam String sobrenome) {

        List<Agendamento> agendamentos = buscarAgendamentosUseCase.execute(nome, sobrenome);

        // Mapeia a lista de Entidades de Negócio para a lista de DTOs de Resposta
        List<AgendamentoResponse> responseList = agendamentos.stream()
                .map(AgendamentoResponse::fromDomain)
                .collect(Collectors.toList());

        // Retorna a lista e o status HTTP 200 OK
        return ResponseEntity.ok(responseList);
    }
}