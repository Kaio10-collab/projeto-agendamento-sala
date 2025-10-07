package com.agendamento.salas.application.usecase;

import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;

// Aqui teremos a interface que define o contrato que a camada externa (Controller REST) usará para interagir com o Use Case.
public interface CriarAgendamentoMapper {

    Agendamento execute(CriarAgendamentoRequest request);
}
// A interface vai garantir que o Controller (na Infraestrutura) saiba como chamar o Use Case, sem conhecer os detalhes da sua implementação interna.