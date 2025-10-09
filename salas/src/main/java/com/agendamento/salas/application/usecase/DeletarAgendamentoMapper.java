package com.agendamento.salas.application.usecase;

public interface DeletarAgendamentoMapper {
    void execute(Long id, String nomePessoa, String sobrenomePessoa, String nomeSala);
}
