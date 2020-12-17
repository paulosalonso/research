package com.github.paulosalonso.research.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.github.paulosalonso.research")
@EnableJpaRepositories(basePackages = "com.github.paulosalonso.research")
@EntityScan(basePackages = "com.github.paulosalonso.research")
public class ResearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResearchApplication.class, args);
	}

}
