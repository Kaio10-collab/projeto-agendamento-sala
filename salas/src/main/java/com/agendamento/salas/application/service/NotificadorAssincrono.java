package com.agendamento.salas.application.service;

import com.agendamento.salas.domain.entity.Agendamento;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificadorAssincrono {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    // A anotação @Async garante que este método será executado em um Thread Pool separado,
    // não bloqueando o Thread principal do Controller/Use Case.
    @Async
    public void notificarAgendamentoCriado(Agendamento agendamento) {

        var inicioFormatado = agendamento.dataHoraInicio().format(formatter);
        var fimFormatado = agendamento.dataHoraFim().format(formatter);

        var mensagem = String.format(
                "*** Processo Assíncrono Iniciado ***\n" +
                        "Notificação enviada para %s %s (Tel: %s).\n" +
                        "Sala: %s | Início: %s | Fim: %s\n" +
                        "Tarefa não bloqueante concluída.\n" +
                        "*** Processo Assíncrono Finalizado ***",
                agendamento.nomePessoa(), agendamento.sobrenomePessoa(), agendamento.telefonePessoa(),
                agendamento.nomeSala(), inicioFormatado, fimFormatado
        );

        // Simulação de uma tarefa demorada (Ex: Chamada a um serviço de e-mail)
        try {
            Thread.sleep(3000); // Dorme por 3 segundos para simular a latência
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(mensagem);
    }
}
// será responsável por enviar notificações, e o marcaremos para ser executado de forma assíncrona.