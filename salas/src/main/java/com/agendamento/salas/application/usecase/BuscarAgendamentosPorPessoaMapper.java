package com.agendamento.salas.application.usecase;

import com.agendamento.salas.domain.entity.Agendamento;

import java.util.List;

public interface BuscarAgendamentosPorPessoaMapper {
    List<Agendamento> execute(String nome, String sobrenome);
}
