package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.service.ValidadorDeAgendamento;
import com.agendamento.salas.application.usecase.AtualizarAgendamentoMapper;
import com.agendamento.salas.application.usecase.dto.AtualizarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtualizarAgendamentoUseCaseImpl implements AtualizarAgendamentoMapper {

    private final AgendamentoGateway agendamentoGateway;
    private final ValidadorDeAgendamento validador;

    @Override
    public Agendamento execute(AtualizarAgendamentoRequest request) {

        Optional<Agendamento> agendamentoOpt = agendamentoGateway.buscarPorId(request.idAgendamento());

        if (agendamentoOpt.isEmpty()) {
            throw new RegraDeNegocioException("Agendamento não encontrado.");
        }

        Agendamento agendamentoAntigo = agendamentoOpt.get();

        validarRegra24Horas(agendamentoAntigo.dataHoraInicio());

        Agendamento agendamentoNovo = new Agendamento(
                agendamentoAntigo.id(),
                agendamentoAntigo.nomeSala(),
                agendamentoAntigo.nomePessoa(),
                agendamentoAntigo.sobrenomePessoa(),
                agendamentoAntigo.telefonePessoa(),
                request.novaDataHoraInicio(),
                request.novaDataHoraFim()
        );

        if (!agendamentoNovo.periodoValido() || !agendamentoNovo.validoParaAgendamento()) {
            throw new RegraDeNegocioException("O novo período de agendamento é inválido.");
        }

        validador.validarHorarioDeFuncionamento(
                agendamentoNovo.dataHoraInicio(), agendamentoNovo.dataHoraFim());

        validador.verificarDisponibilidadeDeSala(agendamentoNovo);

        // TODO: Próximos passos] Chamada Assíncrona e Mensageria (Notificação de Alteração)

        return agendamentoGateway.salvar(agendamentoNovo);
    }

    // Método que implementa a regra das 24h
    private void validarRegra24Horas(LocalDateTime dataHoraAgendamento) {
        LocalDateTime limiteCancelamento = dataHoraAgendamento.minusHours(24);

        if (LocalDateTime.now().isAfter(limiteCancelamento)) {
            throw new RegraDeNegocioException(
                    "Modificações/Cancelamentos devem ser feitos com no mínimo 24 horas de antecedência. " +
                            "O limite para este agendamento era: " + limiteCancelamento
            );
        }
    }
}
