package com.agendamento.salas.application.usecase.dto;

import java.time.LocalDateTime;
// representa os dados que o cliente (Controller REST) enviará.
public record CriarAgendamentoRequest(String nomeSala,
                                      String nomePessoa,
                                      String sobrenomePessoa,
                                      String telefonePessoa,
                                      LocalDateTime dataHoraInicio,
                                      LocalDateTime dataHoraFim) {
}
