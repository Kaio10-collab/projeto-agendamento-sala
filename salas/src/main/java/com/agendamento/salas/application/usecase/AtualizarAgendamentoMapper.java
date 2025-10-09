package com.agendamento.salas.application.usecase;

import com.agendamento.salas.application.usecase.dto.AtualizarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;

public interface AtualizarAgendamentoMapper {
    Agendamento execute(AtualizarAgendamentoRequest request);
}
