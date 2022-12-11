package com.poo.projetfinal.Controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poo.projetfinal.ProjetfinalApplication;
import com.poo.projetfinal.Recette;
import com.poo.projetfinal.User;
import com.poo.projetfinal.Exceptions.BadFormatException;
import com.poo.projetfinal.Exceptions.BadUserException;
import com.poo.projetfinal.Exceptions.EmptyFieldsException;
import com.poo.projetfinal.Exceptions.NumberException;

@RestController
public class Admin {

	@GetMapping("/admin")
	public ModelAndView profil(HttpServletRequest request, @Nullable @RequestParam("accr") String acronym) {

		@SuppressWarnings("unchecked")
		ArrayList<String> list = (ArrayList<String>) request.getSession().getAttribute("UID");

		try {
			User u = new User(list.get(0));
			if (!u.getMail().equals("admin@foodlovers.ca")) {
				return new ModelAndView("error", HttpStatus.UNAUTHORIZED);
			}
		} catch (BadUserException e) {
			return new ModelAndView("error", HttpStatus.UNAUTHORIZED);
		}

		var mav = new ModelAndView("admin");

		// Pattern

		Index.handleAcronym(acronym, mav);

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
	public RedirectView SubmitRecette(HttpServletRequest request, String titre, String prix,
			String duree,
			@RequestParam("image") MultipartFile image, String ingredients, String prepa) {

		try {
			if (titre.isEmpty() || prix.isEmpty() || duree.isEmpty() || image.isEmpty() || ingredients.isEmpty()
					|| prepa.isEmpty()) {
				throw new EmptyFieldsException();
			}
		} catch (EmptyFieldsException e) {
			return new RedirectView("/admin?accr=" + e.getAcronym());
		}

		try {
			if (Integer.parseInt(duree) < 0 || Integer.parseInt(prix) < 0) {
				throw new NumberException();
			}
		} catch (NumberFormatException e) {
			NumberException e1 = new NumberException();
			return new RedirectView("/admin?accr=" + e1.getAcronym());
		}

		String type = image.getContentType();
		if (type == null)
			type = "";

		System.out.println("Content-Type : " + type);

		try {
		if (type.equals("image/png")) {
			try {
				File file = new File("src/main/resources/static/tmp.png");

				try (OutputStream os = new FileOutputStream(file)) {
					os.write(image.getBytes());
				}

				BufferedImage image2 = ImageIO.read(new File("src/main/resources/static/tmp.png"));

				Recette recette = new Recette(titre, Integer.parseInt(prix), Integer.parseInt(duree), ingredients, prepa, image2);
				recette.CreateEntry();

				File f2 = new File("src/main/resources/static/tmp.png");
				f2.delete();
			} catch (IOException e) {

			}
		} else if (type.equals("image/jpeg")) {
			try {

				// On enregistre l'image JPG

				File file = new File("src/main/resources/static/tmp.jpg");

				try (OutputStream os = new FileOutputStream(file)) {
					os.write(image.getBytes());
				}

				// On recup l'image JPG

				BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/static/tmp.jpg"));

				// On convertit

				File file2 = new File("src/main/resources/static/new.png");
				ImageIO.write(bufferedImage, "png", file2);

				// On recup l'image PNG

				BufferedImage bufferedImage2 = ImageIO.read(new File("src/main/resources/static/new.png"));

				Recette recette = new Recette(titre, Integer.parseInt(prix), Integer.parseInt(duree), ingredients, prepa, bufferedImage2);
				recette.CreateEntry();

				File f1 = new File("src/main/resources/static/new.png");
				f1.delete();

				File f2 = new File("src/main/resources/static/tmp.jpg");
				f2.delete();

			} catch (IOException e) {
			}

		} else {
			throw new BadFormatException();
		}
	}catch(BadFormatException e) {
		return new RedirectView("/admin?accr="+e.getAcronym());
	}

	return new RedirectView("/admin");
	}

}
