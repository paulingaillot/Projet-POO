package com.poo.projetfinal.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poo.projetfinal.ProjetfinalApplication;
import com.poo.projetfinal.User;
import com.poo.projetfinal.Exceptions.BadEmailException;
import com.poo.projetfinal.Exceptions.BadUserException;
import com.poo.projetfinal.Exceptions.EmptyFieldsException;
import com.poo.projetfinal.Exceptions.NumberException;

@RestController
public class Profil {

	@GetMapping("/profil")
	public ModelAndView profil(HttpServletRequest request, @Nullable @RequestParam("accr") String acronym) {

		if (request.getSession().getAttribute("UID") != null) {
			var mav = new ModelAndView("profil");

			@SuppressWarnings("unchecked")
			List<String> token = (List<String>) request.getSession().getAttribute("UID");

			Index.handleAcronym(acronym, mav);

			// Infos Users

			User u = null;
			try {
				u = new User(token.get(0));

				mav.addObject("nom", u.getNom());
				mav.addObject("prenom", u.getPrenom());
				mav.addObject("mail", u.getMail());
				mav.addObject("budget", u.getBudget());
				mav.addObject("temps", u.getTemps());

			} catch (BadUserException e) {
				e.printStackTrace();
			}

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
		} else {
			return new ModelAndView("error", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/ChangeNom")
	public RedirectView ChangeNom(HttpServletRequest request, String nom) {
		@SuppressWarnings("unchecked")
		List<String> token = (List<String>) request.getSession().getAttribute("UID");

		try {
			if (nom.isEmpty())
				throw new EmptyFieldsException();
		} catch (EmptyFieldsException e) {
			return new RedirectView("/profil?accr=" + e.getAcronym());
		}

		try {
			User u = new User(token.get(0));
			u.setNom(nom);
			u.updateDatabase();
		} catch (BadUserException e) {
			e.printStackTrace();
		}
		return new RedirectView("/");
	}

	@GetMapping("/ChangePrenom")
	public RedirectView ChangePrenom(HttpServletRequest request, String prenom) {
		@SuppressWarnings("unchecked")
		List<String> token = (List<String>) request.getSession().getAttribute("UID");

		try {
			if (prenom.isEmpty())
				throw new EmptyFieldsException();
		} catch (EmptyFieldsException e) {
			return new RedirectView("/profil?accr=" + e.getAcronym());
		}

		try {
			User u = new User(token.get(0));
			u.setPrenom(prenom);
			u.updateDatabase();
		} catch (BadUserException e) {
			e.printStackTrace();
		}
		return new RedirectView("/");
	}

	@GetMapping("/ChangeMail")
	public RedirectView ChangeMail(HttpServletRequest request, String mail) {
		@SuppressWarnings("unchecked")
		List<String> token = (List<String>) request.getSession().getAttribute("UID");

		try {
			if (mail.isEmpty())
				throw new EmptyFieldsException();
		} catch (EmptyFieldsException e) {
			return new RedirectView("/profil?accr=" + e.getAcronym());
		}

		try {
			if (!mail.contains("@") || !mail.contains("."))
				throw new BadEmailException();
		} catch (BadEmailException e) {
			return new RedirectView("/profil?accr=" + e.getAcronym());
		}

		try {
			User u = new User(token.get(0));
			u.setMail(mail);
			u.updateDatabase();
		} catch (BadUserException e) {
			e.printStackTrace();
		}
		return new RedirectView("/");
	}

	@GetMapping("/ChangeBudget")
	public RedirectView ChangeBudget(HttpServletRequest request, String budget) {
		@SuppressWarnings("unchecked")
		List<String> token = (List<String>) request.getSession().getAttribute("UID");

		try {
			if (Integer.parseInt(budget) < 0)
				throw new NumberException();
		} catch (NumberFormatException e) {
			NumberException e1 = new NumberException();
			return new RedirectView("/profil?accr=" + e1.getAcronym());
		}

		try {
			User u = new User(token.get(0));
			u.setBudget(Integer.parseInt(budget));
			u.updateDatabase();
		} catch (BadUserException e) {
			e.printStackTrace();
		}
		return new RedirectView("/");
	}

	@GetMapping("/ChangeTemps")
	public RedirectView ChangeTemps(HttpServletRequest request, String temps) {
		@SuppressWarnings("unchecked")
		List<String> token = (List<String>) request.getSession().getAttribute("UID");

		try {
			if (Integer.parseInt(temps) < 0)
				throw new NumberException();
		} catch (NumberFormatException e) {
			NumberException e1 = new NumberException();
			return new RedirectView("/profil?accr=" + e1.getAcronym());
		}

		try {
			User u = new User(token.get(0));
			u.setTemps(Integer.parseInt(temps));
			u.updateDatabase();
		} catch (BadUserException e) {
			e.printStackTrace();
		}
		return new RedirectView("/");
	}
}
