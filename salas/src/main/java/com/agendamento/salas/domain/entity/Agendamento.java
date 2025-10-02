package com.agendamento.salas.domain.entity;

import java.time.Duration;
import java.time.LocalDateTime;

// Usaremos record para garantir a imutabilidade e concisão
public record Agendamento(

        Long id,
        String nomeSala,
        String nomePessoa,
        String sobrenomePessoa,
        String telefonePessoa,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
    // Regra de Negócio Pura: Agendamento deve ter pelo menos 30 minutos
    public boolean validoParaAgendamento() {
        if (dataHoraInicio == null || dataHoraFim == null) {
            return false;
        }
        // Vamos usar o Duration para maior clareza e precisão
        Duration duration = Duration.between(dataHoraInicio, dataHoraFim);
        return !duration.isNegative() && duration.toMinutes() >= 30;
    }

    // Regra de Negócio Pura: Valida se o período de agendamento está invertido.
    // Esse método garante que o período de tempo seja lógico, ou seja, que a data/hora de fim seja depois da data/hora de início.
    public boolean periodoValido() {
        if (dataHoraInicio == null || dataHoraFim == null) {
            return false;
        }
        return dataHoraFim.isAfter(dataHoraInicio);
    }
}
