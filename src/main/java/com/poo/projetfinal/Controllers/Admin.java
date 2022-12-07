package com.poo.projetfinal.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poo.projetfinal.ProjetfinalApplication;
import com.poo.projetfinal.Recette;

@RestController
public class Admin {

    @GetMapping("/admin")
	public ModelAndView profil(HttpServletRequest request) {
        var mav = new ModelAndView("admin");

		// Pattern

		mav.addObject("head", ProjetfinalApplication.pattern.getHead());
		mav.addObject("header", ProjetfinalApplication.pattern.getHeader());
		mav.addObject("footer", ProjetfinalApplication.pattern.getFooter());

        // Mode sombre

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 16 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}

        return mav;
    }

	@PostMapping("/SubmitRecette")
	public RedirectView SubmitRecette(HttpServletRequest request, String titre, int prix, int duree, @RequestParam("image") MultipartFile image, String ingredients, String prepa) {

		Recette recette = new Recette(titre, prix, duree, ingredients, prepa, image);
		recette.CreateEntry();
		

        return new RedirectView("/");
    }

}
