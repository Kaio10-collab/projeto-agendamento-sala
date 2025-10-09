package com.agendamento.salas.application.service;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.domain.exception.RegraDeNegocioException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidadorDeAgendamento {

    private final AgendamentoGateway agendamentoGateway;
    private final List<String> salasValidas = List.of("Stranger", "Thor", "Escarlate");

    public ValidadorDeAgendamento(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    // --- MÉTODOS PÚBLICOS DE VALIDAÇÃO REUTILIZÁVEIS ---

    public void validarHorarioDeFuncionamento(LocalDateTime inicio, LocalDateTime fim) {
        DayOfWeek diaInicio = inicio.getDayOfWeek();

        if (diaInicio == DayOfWeek.SUNDAY) {
            throw new RegraDeNegocioException("A sala não funciona aos Domingos.");
        }

        // Regra: Seg a Sex (08h às 18h)
        if (diaInicio != DayOfWeek.SATURDAY) {
            if (inicio.getHour() < 8 || fim.getHour() > 18 || (fim.getHour() == 18 && fim.getMinute() > 0)) {
                throw new RegraDeNegocioException("Agendamentos de Segunda a Sexta devem ser entre 08:00 e 18:00.");
            }
        }

        // Regra: Sábado (08h às 12h)
        if (diaInicio == DayOfWeek.SATURDAY) {
            if (inicio.getHour() < 8 || fim.getHour() > 12 || (fim.getHour() == 12 && fim.getMinute() > 0)) {
                throw new RegraDeNegocioException("Agendamentos de Sábado devem ser entre 08:00 e 12:00.");
            }
        }

        if (!inicio.toLocalDate().isEqual(fim.toLocalDate())) {
            throw new RegraDeNegocioException("Agendamentos não podem começar em um dia e terminar no outro.");
        }
    }

    public void verificarDisponibilidadeDeSala(Agendamento agendamento) {
        // Esta validação precisa do Gateway para checar o banco
        List<Agendamento> conflitos = agendamentoGateway.buscarConflitos(
                agendamento.nomeSala(),
                agendamento.dataHoraInicio(),
                agendamento.dataHoraFim()
        );

        if (!conflitos.isEmpty()) {
            LocalDateTime fimConflito = conflitos.get(0).dataHoraFim();
            throw new RegraDeNegocioException(
                    "Sala " + agendamento.nomeSala() + " está ocupada. Ficará livre após: " + fimConflito
            );
        }
    }

    public void validarNomeSala(String nomeSala) {
        if (!salasValidas.contains(nomeSala)) {
            throw new RegraDeNegocioException("Sala inválida. Escolha entre: " + String.join(", ", salasValidas));
        }
    }
}
