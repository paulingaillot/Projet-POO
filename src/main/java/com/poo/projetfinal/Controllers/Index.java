package com.poo.projetfinal.Controllers;

import com.poo.projetfinal.RecipeAffinity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poo.projetfinal.ProjetfinalApplication;
import com.poo.projetfinal.Recette;
import com.poo.projetfinal.User;
import com.poo.projetfinal.Exceptions.BadEmailException;
import com.poo.projetfinal.Exceptions.BadFormatException;
import com.poo.projetfinal.Exceptions.BadPasswordException;
import com.poo.projetfinal.Exceptions.BadUserException;
import com.poo.projetfinal.Exceptions.EmptyFieldsException;
import com.poo.projetfinal.Exceptions.NumberException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

@RestController
public class Index {

	@GetMapping("/")
	public ModelAndView IndexPage(HttpServletRequest request) {

		var mav = new ModelAndView("index");

		System.out.println("- DEBUG : " + request.getSession().getAttributeNames().hasMoreElements());
		if (request.getSession().getAttribute("UID") != null) {
			@SuppressWarnings("unchecked")
			List<String> token = (List<String>) request.getSession().getAttribute("UID");

			try {
				System.out.print("Token : " + token.get(0));
				User user = new User(token.get(0));

				mav.addObject("recipesAffinity", getBestRecipes(user.getMail()));
				mav.addObject("recettes", null);
				mav.addObject("username", "Bonjour " + user.getPrenom());
				mav.addObject("message", "");
			} catch (BadUserException e) {
				e.printStackTrace();
			}
		} else {
			mav.addObject("recettes", getLastRecipes());
			mav.addObject("recipesAffinity", null);
			mav.addObject("username", "Bienvenue ");
			mav.addObject("message", "Connecte-toi ou créé un compte pour découvrir de nouvelles recettes");
		}

		// Pattern

//		mav.addObject("head", ProjetfinalApplication.pattern.getHead());
//		mav.addObject("header", ProjetfinalApplication.pattern.getHeader());
//		mav.addObject("footer", ProjetfinalApplication.pattern.getFooter());

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
	public ModelAndView Connexion(HttpServletRequest request,
								  @Nullable @RequestParam("accr") String acronym) {
		var mav = new ModelAndView("connexion");

		handleAcronym(acronym, mav);


		handleAcronym(acronym, mav);

		// Pattern
		mav.addObject("head", ProjetfinalApplication.pattern.getHead());

		mav.addObject("header", ProjetfinalApplication.pattern.getHeader());

		mav.addObject("footer", ProjetfinalApplication.pattern.getFooter());

		// Mode Sombre

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 16 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}

		return mav;
	}

	@PostMapping("/SubmitConnexion")
	public RedirectView SubmitConnexion(String mail, String password) {
		String UID = "";
		if (mail != null && password != null) {

			try {
				User user = new User(mail, password);
				UID = user.generateUniqueID();
			} catch (BadPasswordException e) {
				return new RedirectView("/connexion?accr=" + e.getAcronym());
			} catch (BadUserException e) {
				return new RedirectView("/connexion?accr=" + e.getAcronym());
			}
			return new RedirectView("/addNote?key=UID&note=" + UID + "&redirectPage=/"); // connexion valide
		}
		return new RedirectView("/connexion"); // champs incomplets
	}

	@GetMapping("/inscription")
	public ModelAndView Inscription(@Nullable @RequestParam("accr") String acronym) {
		var mav = new ModelAndView("inscription");

		handleAcronym(acronym, mav);

		// Pattern

		mav.addObject("head", ProjetfinalApplication.pattern.getHead());
		mav.addObject("header", ProjetfinalApplication.pattern.getHeader());
		mav.addObject("footer", ProjetfinalApplication.pattern.getFooter());

		// Mode Sombre

		SimpleDateFormat s = new SimpleDateFormat("HH");
		Date date = new Date();

		if (Integer.parseInt(s.format(date)) >= 16 || Integer.parseInt(s.format(date)) < 8) {
			mav.addObject("background", "bg-dark text-white");
		} else {
			mav.addObject("background", "bg-white text-dark");
		}

		return mav;
	}

	@PostMapping("/SubmitInscription")
	public RedirectView SubmitInscription(String mail, String password, String confirm_password, String nom,
			String prenom, String age, String sexe, String budget, String temps) {

		try {
			if (mail.isEmpty() || password.isEmpty() || prenom.isEmpty() || confirm_password.isEmpty() || nom.isEmpty()
					|| age.isEmpty()
					|| sexe.isEmpty() || budget.isEmpty() || temps.isEmpty() || password.isEmpty()
					|| confirm_password.isEmpty()) {
				throw new EmptyFieldsException();
			}
		} catch (EmptyFieldsException e) {
			return new RedirectView("/inscription?accr=" + e.getAcronym());
		}

		try {
			if (!mail.contains("@") || !mail.contains(".")) {
				throw new BadEmailException();
			}
		} catch (BadEmailException e) {
			return new RedirectView("/inscription?accr=" + e.getAcronym());
		}

		try {
			Integer.parseInt(age);
			if (Integer.parseInt(age) < 0 || Integer.parseInt(age) > 100) {
				throw new NumberException();
			}
		} catch (NumberFormatException e) {
			NumberException e1 = new NumberException();
			return new RedirectView("/inscription?accr=" + e1.getAcronym());
		}

		try {
			if (!password.equals(confirm_password)) {
				throw new BadPasswordException();
			}
		} catch (BadPasswordException e) {
			return new RedirectView("/inscription?accr=" + e.getAcronym());
		}

		String encoded_password = Base64.getEncoder().withoutPadding().encodeToString(password.getBytes());
		new User(mail, encoded_password, nom, prenom, Integer.parseInt(age), sexe.charAt(0),
				Integer.parseInt(budget), Integer.parseInt(temps));

		return new RedirectView("/");

	}

	@GetMapping("/recettealea")
	public RedirectView recetteAlea(HttpServletRequest request) throws SQLException {

		ResultSet recettes = ProjetfinalApplication.sql.getRecettes();
		int size = 0;
		while (recettes.next()) {
			size++;
		}

		int alea = 1 + (int) (Math.random() * ((size - 1) + 1));
		ProjetfinalApplication.sql.close();
		return new RedirectView("/recette?id_recette=" + alea);
	}

	public ArrayList<RecipeAffinity> getBestRecipes(String mail) {

		ArrayList<RecipeAffinity> map_unsorted = new ArrayList<>();
		try {

			ResultSet result = ProjetfinalApplication.sql.getUser(mail);

			result.next();
			int temps = Integer.parseInt(result.getString("temps"));
			int budget = Integer.parseInt(result.getString("budget"));
			ProjetfinalApplication.sql.close();

			result = ProjetfinalApplication.sql.getRecettes();

			while (result.next()) {
				String nom = result.getString("nom");
				int temps_recette = Integer.parseInt(result.getString("temps"));
				int budget_recette = Integer.parseInt(result.getString("budget"));
				int id_recette = Integer.parseInt(result.getString("id"));

				Recette recipe = new Recette(nom, temps_recette, budget_recette, id_recette);
				int affinity = userCompare(temps, temps_recette, budget, budget_recette);
				map_unsorted.add(new RecipeAffinity(recipe,affinity));
			}

			@SuppressWarnings("unchecked")
//			HashMap<Recette, Integer> sorted_map = sortValues(map_unsorted);

			ArrayList<RecipeAffinity> sorted = (ArrayList<RecipeAffinity>) map_unsorted.stream()
					.sorted(Comparator.comparingInt(RecipeAffinity::getScore)).toList();

			for (RecipeAffinity ra: sorted) {
				try {
					byte[] imagetab = ProjetfinalApplication.sql.chargeIMG(ra.getRecipe().getId() + "");
					String response = Base64.getEncoder().encodeToString(imagetab);
					String imagevalue = "data:image/png;base64," + response + "";
					ra.getRecipe().setImageRendered(imagevalue);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			ProjetfinalApplication.sql.close();

			return sorted;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map_unsorted;
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

		int dif = (int) (Math.abs(temps_recette - temps_user) * 0.2)
				+ (int) (Math.abs(budget_recette - budget_user) * 0.3);

		System.out.println("-----------");
		System.out.println(temps_user + " / " + temps_recette);
		System.out.println((int) (0.5 * Math.sqrt(Math.abs(Math.pow(temps_user, 2) - Math.pow(temps_recette, 2)))));
		System.out.println(budget_recette + " / " + budget_user);
		System.out.println(Math.abs(Math.pow(budget_recette, 2) - Math.pow(budget_user, 2)) + "");
		System.out.println((int) (0.3 * Math.sqrt(Math.abs(Math.pow(budget_recette, 2) - Math.pow(budget_user, 2)))));
		System.out.println(dif);

		int pourcent = 100 - (int) (dif);
		if (pourcent < 0)
			pourcent = 0;

		return pourcent;
	}

	public ArrayList<Recette> getLastRecipes() {
		ArrayList<Recette> returned = new ArrayList<>();
		try {

			ResultSet result = ProjetfinalApplication.sql.getRecettes();
			while (result.next()) {

				int id = result.getInt("id");
				String nom = result.getString("nom");
				int temps = result.getInt("temps");
				int prix = result.getInt("budget");
				String id_recette = result.getString("id");
				returned.add(new Recette(nom,temps,prix,id));

				String imagevalue = "";
				try {
					byte[] imagetab = ProjetfinalApplication.sql.chargeIMG(id_recette + "");
					String response = Base64.getEncoder().encodeToString(imagetab);
					imagevalue = "data:image/png;base64," + response;
				} catch (Exception e) {
					e.printStackTrace();
				}

				returned.get(returned.size()-1).setImageRendered(imagevalue);
			}
			ProjetfinalApplication.sql.close();

			return returned;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returned;
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


	public static String handleErrorMessage(String acronym) {
		if (acronym.equals(BadUserException.ACRONYM)) {
			return new BadUserException().getMessage();
		}
		if (acronym.equals(BadPasswordException.ACRONYM)) {
			return new BadPasswordException().getMessage();
		}
		if (acronym.equals(EmptyFieldsException.ACRONYM)) {
			return new EmptyFieldsException().getMessage();
		}
		if (acronym.equals(NumberException.ACRONYM)) {
			return new NumberException().getMessage();
		}
		if (acronym.equals(BadEmailException.ACRONYM)) {
			return new BadEmailException().getMessage();
		}
		if (acronym.equals(BadFormatException.ACRONYM)) {
			return new BadFormatException().getMessage();
		}
		return "";
	}


	public static void handleAcronym(String acronym, ModelAndView mav) {
		String className = "d-block";
		if (acronym == null) {
			acronym = "";
			className = "d-none";
		}
		mav.addObject("ErrorMessage", handleErrorMessage(acronym));
		mav.addObject("ErrorMessageClassName", className);
	}
}
