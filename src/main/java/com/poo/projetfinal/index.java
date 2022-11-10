package com.poo.projetfinal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class index {

	public String readServletCookie(HttpServletRequest request, String name) {
		try {
			return Arrays.stream(request.getCookies())
					.filter(cookie -> name.equals(cookie.getName()))
					.map(Cookie::getValue)
					.findAny()
					.get();
		} catch (NullPointerException e) {
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
		if (readServletCookie(request, "username") != null) {
			String mail = readServletCookie(request, "mail");
			mav.addObject("recettes", getBestRecipes(mail));
			mav.addObject("recommandation", "<th>Recommandation</th>");
			name = readServletCookie(request, "username");
			mav.addObject("username", name);
			mav.addObject("message", "");
		} else {
			mav.addObject("recettes", getLastRecipes());
			mav.addObject("username", name);
			mav.addObject("recommandation", "");
			mav.addObject("message", "<p>Connecte-toi pour découvrir de nouvelles recettes</p>");
		}

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 20 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}
		return mav;
	}

	@GetMapping("/connexion")
	public ModelAndView Connexion() {
		var mav = new ModelAndView("connexion");

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 20 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}

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

				PreparedStatement st = ct
						.prepareStatement("SELECT prenom, password FROM users WHERE mail='" + nom + "';");
				ResultSet result = st.executeQuery();
				result.next();
				String pwd = result.getString("password");
				String prenom = result.getString("prenom");
				st.close();
				if (pwd.equals(password)) {
					// create a cookie
					Cookie cookie = new Cookie("username", prenom);
					Cookie cookie2 = new Cookie("mail", nom);
					response.addCookie(cookie);
					response.addCookie(cookie2);

					return new RedirectView("/");
				} else {
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

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 20 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}

		return mav;
	}

	@PostMapping("/SubmitInscription")
	public RedirectView SubmitInscription(String mail, String password, String confirm_password, String nom,
			String prenom, String age, String sexe, String budget, String temps) {
		System.out.println(nom);
		System.out.println(password);
		System.out.println(prenom);
		System.out.println(mail);
		System.out.println(confirm_password);
		System.out.println(age);
		System.out.println(sexe);
		System.out.println(budget);
		System.out.println(temps);

		// mail = mail.replace("@", "at");

		if (mail != null && password != null && mail != null && confirm_password != null && nom != null && age != null
				&& sexe != null && budget != null && temps != null && password.equals(confirm_password)) {
			String url = "jdbc:mysql://127.0.0.1:3306/test";
			String username = "new_user";
			String passwd = "test";

			Connection ct;
			try {
				ct = DriverManager.getConnection(url, username, passwd);
				System.out.println("Connexion a la base de donnée établie.");

				Statement st = ct.createStatement();
				st.execute(
						"INSERT INTO `test`.`users` (`mail`,`password`, `nom`, `prenom`, `age`, `sexe`, `budget`, `temps`)VALUES('"
								+ mail + "', '" + password + "', '" + nom + "', '" + prenom + "', "
								+ Integer.parseInt(age) + ", '" + sexe + "', " + Integer.parseInt(budget) + ", "
								+ Integer.parseInt(temps) + ");");
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new RedirectView("/");
		}
		return new RedirectView("/Inscription");
	}

	public String getBestRecipes(String mail) {
		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String username = "new_user";
		String passwd = "test";

		Connection ct;
		try {
			ct = DriverManager.getConnection(url, username, passwd);
			System.out.println("Connexion a la base de donnée établie.");

			PreparedStatement st = ct.prepareStatement("SELECT * FROM users WHERE mail='" + mail + "';");
			ResultSet result = st.executeQuery();

			result.next();
			int temps = Integer.parseInt(result.getString("temps"));
			int budget = Integer.parseInt(result.getString("budget"));
			st.close();

			st = ct.prepareStatement("SELECT * FROM recette ORDER BY id DESC;");
			result = st.executeQuery();

			HashMap<Recette, Integer> map_unsorted = new HashMap<>();
			while (result.next()) {
				String nom = result.getString("nom");
				int temps_recette = Integer.parseInt(result.getString("temps"));
				int budget_recette = Integer.parseInt(result.getString("budget"));

				map_unsorted.put(new Recette(nom, temps_recette, budget_recette),
						userCompare(temps, temps_recette, budget, budget_recette));
			}

			HashMap<Recette, Integer> sorted_map = sortValues(map_unsorted);

			String affichage = "";
			int i = 0;
			Set<Entry<Recette, Integer>> set = sorted_map.entrySet();
			Iterator<Entry<Recette, Integer>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry<Recette, Integer> map = (Map.Entry<Recette, Integer>) iterator.next();
				i++;
				affichage += "<tr><th>" + i + "</th><td>" + map.getKey().getNom() + "</td><td>"
						+ map.getKey().getduree() + " min</td><td>" + map.getKey().getBudget()
						+ "€</td><td>" + map.getValue() + "%</td></tr>";
			}
			st.close();

			return affichage;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "error";
	}

	// method to sort values
	@SuppressWarnings("all")
	private static HashMap sortValues(HashMap map) {
		List list = new LinkedList(map.entrySet());
		// Custom Comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});
		// copying the sorted list in HashMap to preserve the iteration order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	public int userCompare(int temps_user, int temps_recette, int budget_user, int budget_recette) {

		int dif = (int) Math.sqrt(Math.abs(Math.pow(temps_user, 2) - Math.pow(temps_recette, 2)))
				+ (int) Math.sqrt(Math.abs(Math.pow(budget_recette, 2) - Math.pow(budget_user, 2)));
		System.out.println(temps_user + " / " + temps_recette);
		System.out.println((int) Math.sqrt(Math.pow(temps_user, 2) - Math.pow(temps_recette, 2)));
		System.out.println(budget_recette + " / " + budget_user);
		System.out.println(Math.sqrt(Math.pow(budget_recette, 2) - Math.pow(budget_user, 2)));

		int pourcent = 100 - (int) (0.5 * dif);
		if (pourcent < 0)
			pourcent = 0;

		return pourcent;
	}

	public String getLastRecipes() {
		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String username = "new_user";
		String passwd = "test";

		Connection ct;
		try {
			ct = DriverManager.getConnection(url, username, passwd);
			System.out.println("Connexion a la base de donnée établie.");

			PreparedStatement st = ct.prepareStatement("SELECT * FROM recette ORDER BY id DESC LIMIT 5;");
			ResultSet result = st.executeQuery();

			String affichage = "";
			int i = 0;
			while (result.next()) {
				String nom = result.getString("nom");
				String temps = result.getString("temps");
				String prix = result.getString("budget");
				i++;
				affichage += "<tr><th>" + i + "</th><td>" + nom + "</td><td>" + temps + " min</td><td>" + prix
						+ "€</td></tr>";
			}
			st.close();

			return affichage;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "error";
	}

}
