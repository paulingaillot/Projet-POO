package com.poo.projetfinal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class index {
    @GetMapping("/")
	public String index() {
		return "Waaaaouhh ! Super experience utilisateur la !";
	}
}
