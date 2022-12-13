package com.poo.projetfinal.Controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ressources {
    @RequestMapping(value = "/ressources/banniere.png", method = RequestMethod.GET,
	produces = MediaType.IMAGE_PNG_VALUE)
	public ClassPathResource getImageAsByteArray(HttpServletResponse response) throws IOException {
		return new ClassPathResource("static/banniere.png");
	}
}
