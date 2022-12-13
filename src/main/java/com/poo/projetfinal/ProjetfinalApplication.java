package com.poo.projetfinal;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.poo.projetfinal.Model.Database;
import com.poo.projetfinal.Model.WebSitePattern;

@SpringBootApplication
public class ProjetfinalApplication {

	public static Database sql;
	public static WebSitePattern pattern;

	public static void main(String[] args) throws Exception {

		// Construction de la classe contenant le pattern du site web

		pattern = new WebSitePattern();

		// On ajoute les images a la BDD

		sql = new Database();

		for (int i = 1; i <= 3; i++) {
			sql.sauveIMG("src/main/resources/static/" + i + ".png", i + "");
		}

		// On demarre Spring

		SpringApplication.run(ProjetfinalApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}

}
