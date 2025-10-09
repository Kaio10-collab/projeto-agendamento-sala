package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.usecase.DeletarAgendamentoMapper;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletarAgendamentoUseCaseImpl implements DeletarAgendamentoMapper {

    private final AgendamentoGateway agendamentoGateway;

    @Override
    public void execute(Long id, String nomePessoa, String sobrenomePessoa, String nomeSala) {

        Optional<Agendamento> agendamentoOpt = agendamentoGateway.buscarPorId(id);

        if (agendamentoOpt.isEmpty()) {
            // Se o agendamento não existe, consideramos a operação como 'sucesso' (Idempotência)
            return;
        }

        Agendamento agendamento = agendamentoOpt.get();

        if (!agendamento.nomePessoa().equalsIgnoreCase(nomePessoa) ||
                !agendamento.sobrenomePessoa().equalsIgnoreCase(sobrenomePessoa) ||
                !agendamento.nomeSala().equalsIgnoreCase(nomeSala)) {

            throw new RegraDeNegocioException("Dados informados não correspondem ao agendamento.");
        }

        long horasParaInicio = ChronoUnit.HOURS.between(LocalDateTime.now(), agendamento.dataHoraInicio());

        if (horasParaInicio < 24) {
            throw new RegraDeNegocioException(
                    "Cancelamento deve ser feito com no mínimo 24 horas de antecedência. Faltam apenas " + horasParaInicio + " horas."
            );
        }

        agendamentoGateway.deletar(agendamento);
    }
}
