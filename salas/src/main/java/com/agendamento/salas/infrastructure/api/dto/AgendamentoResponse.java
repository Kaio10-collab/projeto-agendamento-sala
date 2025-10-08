package com.agendamento.salas.infrastructure.api.dto;

import com.agendamento.salas.domain.entity.Agendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(Long id,
                                  String nomeSala,
                                  String nomePessoa,
                                  String telefonePessoa,
                                  LocalDateTime dataHoraInicio,
                                  LocalDateTime dataHoraFim,
                                  String mensagem) {

    // Método Estático para criar o DTO a partir da Entidade
    public static AgendamentoResponse fromDomain(Agendamento domain) {
        return new AgendamentoResponse(
                domain.id(),
                domain.nomeSala(),
                domain.nomePessoa() + " " + domain.sobrenomePessoa(),
                domain.telefonePessoa(),
                domain.dataHoraInicio(),
                domain.dataHoraFim(),
                // Mensagem de sucesso (Regra de Negócio)
                "Agendado com sucesso! Lembrete: avise com 24h de antecedência para cancelar."
        );
    }
}
// é uma boa prática, a gente criar um DTO de resposta específico.
// Isso impede que o Controller exponha detalhes internos da Entidade e permite que você personalize a resposta.