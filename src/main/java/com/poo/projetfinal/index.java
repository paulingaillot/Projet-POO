package com.poo.projetfinal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class index {

	public String readServletCookie(HttpServletRequest request, String name){
		try {
		return Arrays.stream(request.getCookies())
		  .filter(cookie->name.equals(cookie.getName()))
		  .map(Cookie::getValue)
		  .findAny()
		  .get();
		}catch(NullPointerException e) {
			return null;
		}
	  }

	@GetMapping("/")
	public ModelAndView Index(HttpServletRequest request) {

		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String username = "new_user";
		String passwd = "test";

		Connection ct;
		try {
			ct = DriverManager.getConnection(url, username, passwd);
			System.out.println("Connexion a la base de donnée établie.");

			PreparedStatement st = ct.prepareStatement("SELECT * FROM recette;");
			ResultSet result = st.executeQuery();
			while (result.next()) {
				System.out.println(result.getString("nom"));
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		var mav = new ModelAndView("index");

		String name = "newbie";
		if(readServletCookie(request, "username") != null) {
			name = readServletCookie(request, "username");
			mav.addObject("username", name);
			mav.addObject("message", "");
		}else {
			mav.addObject("username", name);
			mav.addObject("message", "<p>Connecte-toi pour découvrir de nouvelles recettes</p>");
		}

		SimpleDateFormat s = new SimpleDateFormat("HH");
   		Date date = new Date();
    	
		if(Integer.parseInt(s.format(date)) >= 20 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "p-3 mb-2 bg-dark text-white");
		}else {
			mav.addObject("background", "p-3 mb-2 bg-white text-dark");
		}
		return mav;
	}

	@GetMapping("/connexion")
	public ModelAndView Connexion() {
		var mav = new ModelAndView("connexion");
		return mav;
	}

	@PostMapping("/SubmitConnexion")
	public RedirectView SubmitConnexion(String nom, String password, HttpServletResponse response) {
		System.out.println(nom);
		System.out.println(password);
		if (nom != null && password != null) {
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			String username = "new_user";
			String passwd = "test";

			Connection ct;
			try {
				ct = DriverManager.getConnection(url, username, passwd);
				System.out.println("Connexion a la base de donnée établie.");

				PreparedStatement st = ct.prepareStatement("SELECT password FROM users WHERE mail='"+nom+"';");
				ResultSet result = st.executeQuery();
				result.next();
				String pwd = result.getString("password");
				st.close();
				if(pwd.equals(password)) {
					// create a cookie
    				Cookie cookie = new Cookie("username", nom);
					response.addCookie(cookie);
					
					return new RedirectView("/");
				}else {
					return new RedirectView("/connexion");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new RedirectView("/connexion");
	}

	@GetMapping("/inscription")
	public ModelAndView Inscription() {
		var mav = new ModelAndView("inscription");

		return mav;
	}

	@PostMapping("/SubmitInscription")
	public RedirectView SubmitInscription(String nom, String password) {
		System.out.println(nom);
		System.out.println(password);
		if (nom != null && password != null) {
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			String username = "new_user";
			String passwd = "test";

			Connection ct;
			try {
				ct = DriverManager.getConnection(url, username, passwd);
				System.out.println("Connexion a la base de donnée établie.");

				Statement st = ct.createStatement();
				st.execute("INSERT INTO `test`.`users` (`mail`,`password`)VALUES('" + nom + "', '" + password + "');");
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new RedirectView("/");
	}

}
