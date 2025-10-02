package com.agendamento.salas.application.gateway;

import com.agendamento.salas.domain.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoGateway {

    // CRUD: Criar/Atualizar
    // Recebe e retorna o record Agendamento
    Agendamento salvar(Agendamento agendamento);

    // CRUD: Ler
    // Retorna um Optional do record Agendamento
    Optional<Agendamento> buscarPorId(Long id);

    // Regra de Negócio: Verifica se há conflito de agendamento na sala
    // Retorna uma lista de records Agendamento
    List<Agendamento> buscarConflitos(String nomeSala, LocalDateTime inicio, LocalDateTime fim);

    // Regra de Negócio: Buscar por nome e sobrenome da pessoa (Pesquisa)
    // Retorna uma lista de records Agendamento
    List<Agendamento> buscarPorPessoa(String nome, String sobrenome);

    // CRUD: Deletar
    void deletar(Agendamento agendamento);
}
