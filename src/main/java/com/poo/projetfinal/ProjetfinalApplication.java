package com.poo.projetfinal;

import java.sql.ResultSet;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjetfinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetfinalApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {


			// On ajoute les images a la BDD

			Database sql = new Database();
			ResultSet recettes = sql.getRecettes();

			while(recettes.next()) {
				String id = recettes.getString("id");
				sql.sauveIMG("src/main/resources/static/"+id+".png", id);
			}
			sql.close();


			// On demarre Spring

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}
	
}
