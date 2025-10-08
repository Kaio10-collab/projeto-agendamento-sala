package com.agendamento.salas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Habilita o suporte ao caching no Spring
public class SalasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalasApplication.class, args);
	}

}
// como vamos trabalhar com cache, precisamos que o Spring gerencie o cache, para isso precisamos de uma configuração mínima.
// Vamos usar o cache em memória padrão do Spring (baseado em ConcurrentHashMap), que é ótimo para o ambiente de desenvolvimento com H2.