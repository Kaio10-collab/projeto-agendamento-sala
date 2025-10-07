package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.usecase.CriarAgendamentoMapper;
import com.agendamento.salas.application.usecase.dto.CriarAgendamentoRequest;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service // Marca como um componente Spring de Serviço
public class CriarAgendamentoUseCaseImpl implements CriarAgendamentoMapper {

    private final AgendamentoGateway agendamentoGateway;

    // Injeção da Porta (AgendamentoGateway)
    public CriarAgendamentoUseCaseImpl(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    @Override
    public Agendamento execute(CriarAgendamentoRequest request) {

        // 1. Cria a Entidade de Negócio com os dados da requisição
        Agendamento novoAgendamento = new Agendamento(
                null, // ID nulo, será gerado na persistência
                request.nomeSala(),
                request.nomePessoa(),
                request.sobrenomePessoa(),
                request.telefonePessoa(),
                request.dataHoraInicio(),
                request.dataHoraFim()
        );

        // 2. Validações de Domínio (Regras internas da Entidade)
        if (!novoAgendamento.periodoValido()) {
            throw new RegraDeNegocioException("A data de fim deve ser posterior à data de início.");
        }
        if (!novoAgendamento.validoParaAgendamento()) {
            throw new RegraDeNegocioException("O agendamento deve durar no mínimo 30 minutos.");
        }

        // 3. Validação Específica da Aplicação: Horário de Funcionamento
        validarHorarioDeFuncionamento(novoAgendamento.dataHoraInicio(), novoAgendamento.dataHoraFim());

        // 4. Validação Específica da Aplicação: Conflito de Sala
        verificarDisponibilidadeDeSala(novoAgendamento);

        // 5. Validação Específica da Aplicação: Nome da Sala
        validarNomeSala(novoAgendamento.nomeSala());

        return agendamentoGateway.salvar(novoAgendamento);
    }

    // --- Métodos de Validação de Regras de Negócio ---

    private void validarNomeSala(String nomeSala) {
        List<String> salasValidas = List.of("Stranger", "Thor", "Escarlate");
        if (!salasValidas.contains(nomeSala)) {
            throw new RegraDeNegocioException("Sala inválida. Escolha entre: " + String.join(", ", salasValidas));
        }
    }

    private void validarHorarioDeFuncionamento(LocalDateTime inicio, LocalDateTime fim) {
        DayOfWeek diaInicio = inicio.getDayOfWeek();

        // Regra 1: Não funciona Domingo
        if (diaInicio == DayOfWeek.SUNDAY) {
            throw new RegraDeNegocioException("A sala não funciona aos Domingos.");
        }

        // Regra 2: Seg a Sex (08h às 18h)
        if (diaInicio != DayOfWeek.SATURDAY) {
            if (inicio.getHour() < 8 || fim.getHour() > 18 || (fim.getHour() == 18 && fim.getMinute() > 0)) {
                throw new RegraDeNegocioException("Agendamentos de Segunda a Sexta devem ser entre 08:00 e 18:00.");
            }
        }

        // Regra 3: Sábado (08h às 12h)
        if (diaInicio == DayOfWeek.SATURDAY) {
            if (inicio.getHour() < 8 || fim.getHour() > 12 || (fim.getHour() == 12 && fim.getMinute() > 0)) {
                throw new RegraDeNegocioException("Agendamentos de Sábado devem ser entre 08:00 e 12:00.");
            }
        }

        // Regra 4: O agendamento não pode terminar em um dia diferente (para simplificar)
        if (!inicio.toLocalDate().isEqual(fim.toLocalDate())) {
            throw new RegraDeNegocioException("Agendamentos não podem começar em um dia e terminar no outro.");
        }
    }

    private void verificarDisponibilidadeDeSala(Agendamento agendamento) {
        List<Agendamento> conflitos = agendamentoGateway.buscarConflitos(
                agendamento.nomeSala(),
                agendamento.dataHoraInicio(),
                agendamento.dataHoraFim()
        );

        if (!conflitos.isEmpty()) {
            // Se houver conflito, retornamos uma mensagem informativa
            LocalDateTime fimConflito = conflitos.get(0).dataHoraFim();
            throw new RegraDeNegocioException(
                    "Sala " + agendamento.nomeSala() + " está ocupada. Ficará livre após: " + fimConflito
            );
        }
    }
}
