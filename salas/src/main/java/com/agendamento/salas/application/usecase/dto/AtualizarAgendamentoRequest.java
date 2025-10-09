package com.agendamento.salas.application.usecase.dto;

import java.time.LocalDateTime;

public record AtualizarAgendamentoRequest(Long idAgendamento,
                                          String nomeSala, // Incluímos para garantir que a sala está correta
                                          LocalDateTime novaDataHoraInicio,
                                          LocalDateTime novaDataHoraFim) {
}
