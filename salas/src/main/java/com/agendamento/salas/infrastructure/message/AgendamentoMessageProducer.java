package com.agendamento.salas.infrastructure.message;

import com.agendamento.salas.domain.entity.Agendamento;
import com.agendamento.salas.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendamentoMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void enviarMensagemAgendamentoCriado(Agendamento agendamento) {

        // Converte a Entidade de Domínio para uma string JSON (simulação)
        String mensagemJson = String.format(
                "{\"sala\":\"%s\", \"inicio\":\"%s\", \"pessoa\":\"%s %s\"}",
                agendamento.nomeSala(),
                agendamento.dataHoraInicio(),
                agendamento.nomePessoa(),
                agendamento.sobrenomePessoa()
        );

        // Envia a mensagem para o Exchange, usando a Routing Key
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                mensagemJson
        );
        System.out.println("--- Mensagem de agendamento publicada no RabbitMQ ---");
    }
}
