package com.agendamento.salas.infrastructure.api;

import com.agendamento.salas.application.usecase.AtualizarAgendamentoMapper;
import com.agendamento.salas.application.usecase.BuscarAgendamentosPorPessoaMapper;
import com.agendamento.salas.application.usecase.CriarAgendamentoMapper;
import com.agendamento.salas.application.usecase.DeletarAgendamentoMapper;
import com.agendamento.salas.application.usecase.dto.AtualizarAgendamentoRequest;
import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.infrastructure.api.dto.AgendamentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// essa anotação marca a classe como um Controller REST
@RestController
@RequiredArgsConstructor // injeta o constructor para classe atribuidas como final, no caso as classes private final
@RequestMapping("/v1/agendamentos") // Define o caminho base da API
public class AgendamentoController {

    private final CriarAgendamentoMapper criarAgendamentoMapper;
    private final BuscarAgendamentosPorPessoaMapper buscarAgendamentosPorPessoaMapper;
    private final AtualizarAgendamentoMapper atualizarAgendamentoMapper;
    private final DeletarAgendamentoMapper deletarAgendamentoMapper;

    // Endpoint para criar um novo agendamento
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@RequestBody CriarAgendamentoRequest request) {

        // O Controller chama o Use Case, que orquestra a lógica
        Agendamento agendamentoSalvo = criarAgendamentoMapper.execute(request);

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

        List<Agendamento> agendamentos = buscarAgendamentosPorPessoaMapper.execute(nome, sobrenome);

        // Mapeia a lista de Entidades de Negócio para a lista de DTOs de Resposta
        List<AgendamentoResponse> responseList = agendamentos.stream()
                .map(AgendamentoResponse::fromDomain)
                .collect(Collectors.toList());

        // Retorna a lista e o status HTTP 200 OK
        return ResponseEntity.ok(responseList);
    }

    // Endpoint para atualizar agendamentos
    @PutMapping("/{id}") // Usa o ID na URL
    public ResponseEntity<AgendamentoResponse> atualizarAgendamento(
            @PathVariable Long id,
            @RequestBody AtualizarAgendamentoRequest request) {

        // Cria uma requisição com o ID da URL
        AtualizarAgendamentoRequest requestComId = new AtualizarAgendamentoRequest(
                id,
                request.nomeSala(),
                request.novaDataHoraInicio(),
                request.novaDataHoraFim()
        );

        Agendamento agendamentoAtualizado = atualizarAgendamentoMapper.execute(requestComId);
        AgendamentoResponse response = AgendamentoResponse.fromDomain(agendamentoAtualizado);

        // Retorna HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar um agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(
            @PathVariable Long id,
            @RequestParam String nomePessoa,
            @RequestParam String sobrenomePessoa,
            @RequestParam String nomeSala) {

        deletarAgendamentoMapper.execute(id, nomePessoa, sobrenomePessoa, nomeSala);

        // Retorna HTTP 204 No Content (padrão para exclusão bem-sucedida)
        return ResponseEntity.noContent().build();
    }
}