package com.poo.projetfinal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;

import javax.imageio.ImageIO;

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

				BufferedImage image = ImageIO.read(new File("src/main/resources/static/"+id+".png"));
				//System.out.println(image.getWidth());
				byte[] tab = index.toByteArray(image, "png");

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
