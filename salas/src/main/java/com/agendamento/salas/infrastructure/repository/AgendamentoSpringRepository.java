package com.agendamento.salas.infrastructure.repository;

import com.agendamento.salas.infrastructure.repository.model.AgendamentoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoSpringRepository extends JpaRepository<AgendamentoJPA, Long> {

    // Query JPA para verificar se existe um agendamento conflitante
    @Query("SELECT a FROM AgendamentoJPA a WHERE a.nomeSala = :nomeSala AND " +
            "((a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio))")
    List<AgendamentoJPA> findConflitos(String nomeSala, LocalDateTime inicio, LocalDateTime fim);

    List<AgendamentoJPA> findByNomePessoaAndSobrenomePessoa(String nomePessoa, String sobrenomePessoa);
}
