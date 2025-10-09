package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.service.ValidadorDeAgendamento;
import com.agendamento.salas.application.usecase.CriarAgendamentoMapper;
import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarAgendamentoUseCaseImpl implements CriarAgendamentoMapper {

    private final AgendamentoGateway agendamentoGateway;
    private final ValidadorDeAgendamento validador;

    @Override
    public Agendamento execute(CriarAgendamentoRequest request) {

        Agendamento novoAgendamento = new Agendamento(
                null, // ID nulo, será gerado na persistência
                request.nomeSala(),
                request.nomePessoa(),
                request.sobrenomePessoa(),
                request.telefonePessoa(),
                request.dataHoraInicio(),
                request.dataHoraFim()
        );

        if (!novoAgendamento.periodoValido()) {
            throw new RegraDeNegocioException("A data de fim deve ser posterior à data de início.");
        }
        if (!novoAgendamento.validoParaAgendamento()) {
            throw new RegraDeNegocioException("O agendamento deve durar no mínimo 30 minutos.");
        }

        validador.validarHorarioDeFuncionamento(novoAgendamento.dataHoraInicio(), novoAgendamento.dataHoraFim());
        validador.verificarDisponibilidadeDeSala(novoAgendamento);
        validador.validarNomeSala(novoAgendamento.nomeSala());

        return agendamentoGateway.salvar(novoAgendamento);
    }
}
