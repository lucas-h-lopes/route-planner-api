package com.study.projects.percursos_van;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class PercursosVanApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder sab = new SpringApplicationBuilder(PercursosVanApplication.class);
		sab.properties("spring.profiles.active=dev");

		sab.run(args);
	}

	@Bean
	public Random random(){
		return new Random();
	}

}
