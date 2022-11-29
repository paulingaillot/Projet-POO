package com.poo.projetfinal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poo.projetfinal.Exceptions.BadPasswordException;
import com.poo.projetfinal.Exceptions.BadUserException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
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

import javax.imageio.ImageIO;
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

		var mav = new ModelAndView("index");

		String name = "newbie";
		if (readServletCookie(request, "token") != null) {

			String mail = readServletCookie(request, "mail");
			String token = readServletCookie(request, "token");
			try {
				User user = new User(mail, new String(Base64.getDecoder().decode(token)));

				mav.addObject("recettes", getBestRecipes(mail));
				mav.addObject("recommandation", "<th>Recommandation</th>");
				mav.addObject("username", user.getPrenom());
				mav.addObject("message", "");
			} catch (BadPasswordException | BadUserException e) {

			}
		} else {
			mav.addObject("recettes", getLastRecipes());
			mav.addObject("username", name);
			mav.addObject("recommandation", "");
			mav.addObject("message", "<p>Connecte-toi pour découvrir de nouvelles recettes</p>");
		}

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
	public RedirectView SubmitConnexion(String mail, String password, HttpServletResponse response) {
		if (mail != null && password != null) {

			try {
				User user = new User(mail, password);
				Cookie cookie = new Cookie("token", user.getEncodedPassword());
				Cookie cookie2 = new Cookie("mail", user.getMail());
				response.addCookie(cookie);
				response.addCookie(cookie2);
			} catch (BadPasswordException e) {
				return new RedirectView("/connexion");
			} catch (BadUserException e) {
				return new RedirectView("/connexion");
			}
			return new RedirectView("/");
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

		if (mail != null && password != null && mail != null && confirm_password != null && nom != null && age != null
				&& sexe != null && budget != null && temps != null && password.equals(confirm_password)) {

			String encoded_password = Base64.getEncoder().withoutPadding().encodeToString(password.getBytes());
			new User(mail, encoded_password, nom, prenom, Integer.parseInt(age), sexe.charAt(0),
					Integer.parseInt(budget), Integer.parseInt(temps));

			return new RedirectView("/");

		}
		return new RedirectView("/Inscription");
	}

	public String getBestRecipes(String mail) {

		try {

			Database sql = new Database();
			ResultSet result = sql.getUser(mail);

			result.next();
			int temps = Integer.parseInt(result.getString("temps"));
			int budget = Integer.parseInt(result.getString("budget"));
			sql.close();

			result = sql.getRecettes();

			HashMap<Recette, Integer> map_unsorted = new HashMap<>();
			while (result.next()) {
				String nom = result.getString("nom");
				int temps_recette = Integer.parseInt(result.getString("temps"));
				int budget_recette = Integer.parseInt(result.getString("budget"));
				int id_recette = Integer.parseInt(result.getString("id"));

				map_unsorted.put(new Recette(nom, temps_recette, budget_recette, id_recette),
						userCompare(temps, temps_recette, budget, budget_recette));
			}

			@SuppressWarnings("unchecked")
			HashMap<Recette, Integer> sorted_map = sortValues(map_unsorted);

			String affichage = "";
			int i = 0;
			Set<Entry<Recette, Integer>> set = sorted_map.entrySet();
			Iterator<Entry<Recette, Integer>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry<Recette, Integer> map = (Map.Entry<Recette, Integer>) iterator.next();
				i++;

				String imagevalue = "";
				try {
					byte[] imagetab = sql.chargeIMG(map.getKey().getId()+"");
					String response = Base64.getEncoder().encodeToString(imagetab);
					imagevalue = "data:image/png;base64," + response + "";
				} catch(Exception e) {
					e.printStackTrace();
				} 

				affichage += "<div class='card text-bg-secondary' style='width: 18rem;'>"
							+"<img src='"+imagevalue+"' class='card-img-top' alt='img' width=100px >"
							+"<div class='card-body'>"
							+"<h5 class='card-title'>#"+i+" | "+map.getKey().getNom()+"</h5>"
							+"<p>Durée : "+map.getKey().getduree()+"min</p>"
							+"<p>Budget : "+map.getKey().getBudget()+"€</p>"
							+"<p>Recommandation : "+map.getValue()+"%</p>"
							+"</div></div>";


			}
			sql.close();

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
		try {
			Database sql = new Database();

			ResultSet result = sql.getRecettes();

			String affichage = "";
			int i = 0;
			while (result.next()) {
				String nom = result.getString("nom");
				String temps = result.getString("temps");
				String prix = result.getString("budget");
				String id_recette = result.getString("id");
				i++;

				String imagevalue = "";
				try {
					byte[] imagetab = sql.chargeIMG(id_recette+"");
					String response = Base64.getEncoder().encodeToString(imagetab);
					imagevalue = "<img src='data:image/png;base64," + response + "' width=100px />";
				} catch(Exception e) {
					e.printStackTrace();
				} 
				
				affichage += "<tr><th>"+imagevalue+"</th><th>" + i + "</th><td>" + nom + "</td><td>" + temps + " min</td><td>" + prix
						+ "€</td></tr>";
			}
			sql.close();

			return affichage;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "error";
	}

	// convert BufferedImage to byte[]
	public static byte[] toByteArray(BufferedImage bi, String format)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;

	}

	// convert byte[] to BufferedImage
	public static BufferedImage toBufferedImage(byte[] bytes)
			throws IOException {

		InputStream is = new ByteArrayInputStream(bytes);
		BufferedImage bi = ImageIO.read(is);
		return bi;

	}

}
