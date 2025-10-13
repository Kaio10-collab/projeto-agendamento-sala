package com.agendamento.salas.infrastructure.message;

import com.agendamento.salas.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMessageConsumer {

    // Escuta a fila definida na configuração do RabbitMQ
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processarMensagem(String mensagem) {

        // Simulação de processamento de mensagem (Ex: enviar e-mail)
        System.out.println("\n*** Consumidor de Mensagens Recebeu ***");
        System.out.println("Mensagem recebida: " + mensagem);
        System.out.println("Iniciando processamento complexo... (Ex: Envio de E-mail de Confirmação)");
        System.out.println("*** Consumidor de Mensagens Concluído ***\n");
    }
}
