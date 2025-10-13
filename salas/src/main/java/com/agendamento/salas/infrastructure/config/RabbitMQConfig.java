package com.agendamento.salas.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "agendamento.exchange";
    public static final String QUEUE_NAME = "agendamento.confirmacao.queue";
    public static final String ROUTING_KEY = "agendamento.criado";

    // 1. Cria a Fila (Queue)
    @Bean
    public Queue confirmacaoQueue() {
        return new Queue(QUEUE_NAME, true); // durable: true (a fila sobrevive ao restart do broker)
    }

    // 2. Cria o Exchange (Ponto de distribuição)
    @Bean
    public TopicExchange agendamentoExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 3. Liga a Fila ao Exchange através da Routing Key
    @Bean
    public Binding binding(Queue confirmacaoQueue, TopicExchange agendamentoExchange) {
        return BindingBuilder.bind(confirmacaoQueue).to(agendamentoExchange).with(ROUTING_KEY);
    }
}
