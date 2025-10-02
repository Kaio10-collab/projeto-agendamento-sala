package com.agendamento.salas.infrastructure.repository;

import com.agendamento.salas.application.gateway.AgendamentoGateway;
import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.infrastructure.repository.model.AgendamentoJPA;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AgendamentoGatewayImpl implements AgendamentoGateway {

    private final AgendamentoSpringRepository repository;

    public AgendamentoGatewayImpl(AgendamentoSpringRepository repository) {
        this.repository = repository;
    }

    // --- Métodos de Mapeamento (Conversão) ---

    // Converte de AgendamentoJPA para Agendamento (Ao LER do banco)
    private Agendamento toDomain(AgendamentoJPA jpa) {
        // Usamos o construtor canônico (padrão) do record
        return new Agendamento(
                jpa.getId(),
                jpa.getNomeSala(),
                jpa.getNomePessoa(),
                jpa.getSobrenomePessoa(),
                jpa.getTelefonePessoa(),
                jpa.getDataHoraInicio(),
                jpa.getDataHoraFim()
        );
    }

    // Converte de Agendamento para AgendamentoJPA (Ao ESCREVER no banco)
    private AgendamentoJPA toJPA(Agendamento domain) {

        // AgendamentoJPA ainda é uma classe (não record), então usamos o Builder (ou construtor)
        return AgendamentoJPA.builder()
                .id(domain.id()) // Chamada com parênteses, como um método
                .nomeSala(domain.nomeSala())
                .nomePessoa(domain.nomePessoa())
                .sobrenomePessoa(domain.sobrenomePessoa())
                .telefonePessoa(domain.telefonePessoa())
                .dataHoraInicio(domain.dataHoraInicio())
                .dataHoraFim(domain.dataHoraFim())
                .build();
    }

    // --- Implementação do Gateway ---

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        AgendamentoJPA jpa = toJPA(agendamento);
        AgendamentoJPA savedJpa = repository.save(jpa);
        return toDomain(savedJpa); // Retorna a Entidade de Negócio salva
    }

    @Override
    public Optional<Agendamento> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Agendamento> buscarConflitos(String nomeSala, LocalDateTime inicio, LocalDateTime fim) {
        return repository.findConflitos(nomeSala, inicio, fim).stream()
                .map(this::toDomain) // Mapeia a lista de JPA para Domain
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> buscarPorPessoa(String nome, String sobrenome) {
        return repository.findByNomePessoaAndSobrenomePessoa(nome, sobrenome).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Agendamento agendamento) {
        // Para deletar, precisamos passar o modelo JPA
        repository.delete(toJPA(agendamento));
    }
}
