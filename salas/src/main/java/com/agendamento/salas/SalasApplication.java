package com.agendamento.salas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching // Habilita o suporte ao caching no Spring
@EnableAsync // Habilita o uso da anotação @Async
public class SalasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalasApplication.class, args);
	}

}
// como vamos trabalhar com cache, precisamos que o Spring gerencie o cache, para isso precisamos de uma configuração mínima.
// Vamos usar o cache em memória padrão do Spring (baseado em ConcurrentHashMap), que é ótimo para o ambiente de desenvolvimento com H2.

// O uso de chamadas assíncronas é ideal para tarefas que podem levar tempo (como enviar um e-mail,
// gerar um relatório) e não precisam bloquear a resposta HTTP enviada ao usuário.
// Para essas chamadas assincronas, vamos implementar a anotação Async.