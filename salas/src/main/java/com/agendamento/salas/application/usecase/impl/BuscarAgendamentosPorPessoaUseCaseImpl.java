package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.usecase.BuscarAgendamentosPorPessoaMapper;
import com.agendamento.salas.domain.entity.Agendamento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarAgendamentosPorPessoaUseCaseImpl implements BuscarAgendamentosPorPessoaMapper {

    private final AgendamentoGateway agendamentoGateway;

    public BuscarAgendamentosPorPessoaUseCaseImpl(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    @Override
    public List<Agendamento> execute(String nome, String sobrenome) {

        // Chamada simples ao Gateway. O Use Case não sabe que há um cache otimizando esta operação!
        List<Agendamento> agendamentos = agendamentoGateway.buscarPorPessoa(nome, sobrenome);

        // Regra de Negócio: Poderíamos aqui adicionar lógica sobre o que retornar se a lista estiver vazia.
        // Por enquanto, retornaremos a lista (vazia ou preenchida).

        return agendamentos;
    }
}
