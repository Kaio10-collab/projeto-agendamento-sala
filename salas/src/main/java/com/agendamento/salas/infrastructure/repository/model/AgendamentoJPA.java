package com.agendamento.salas.infrastructure.repository.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Data // LEmbrando que essa anotação inclui Getters, Setters, toString, equals, hashCode (Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeSala;

    @Column(name = "nome_pessoa", nullable = false)
    private String nomePessoa;

    @Column(name = "sobrenome_pessoa")
    private String sobrenomePessoa;

    @Column(name = "telefone_pessoa")
    private String telefonePessoa;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;
}
