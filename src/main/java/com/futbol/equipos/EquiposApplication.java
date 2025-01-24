package com.futbol.equipos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan("com.futbol.equipos")
@EntityScan("com.futbol.equipos.entity")
@EnableJpaRepositories("com.futbol.equipos.repository")
@OpenAPIDefinition(info = @Info(title = "API Equipos"))
public class EquiposApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquiposApplication.class, args);
	}

}
