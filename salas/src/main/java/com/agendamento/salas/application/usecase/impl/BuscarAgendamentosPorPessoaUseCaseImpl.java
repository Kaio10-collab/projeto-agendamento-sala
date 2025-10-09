package com.agendamento.salas.application.usecase.impl;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.application.usecase.BuscarAgendamentosPorPessoaMapper;
import com.agendamento.salas.domain.entity.Agendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarAgendamentosPorPessoaUseCaseImpl implements BuscarAgendamentosPorPessoaMapper {

    private final AgendamentoGateway agendamentoGateway;

    @Override
    public List<Agendamento> execute(String nome, String sobrenome) {

        // Chamada simples ao Gateway. O Use Case não sabe que há um cache otimizando esta operação!
        // Regra de Negócio: Poderíamos aqui adicionar lógica sobre o que retornar se a lista estiver vazia.
        // Por enquanto, retornaremos a lista (vazia ou preenchida).
        return agendamentoGateway.buscarPorPessoa(nome, sobrenome);
    }
}
