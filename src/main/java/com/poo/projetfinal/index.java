package com.poo.projetfinal;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.ImageView;

@RestController
public class index {

	@GetMapping("/")
	public ModelAndView index() {

		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String port = "3308";
		String username = "new_user";
		String passwd = "test";

		Connection ct ;
		try {
			ct = DriverManager.getConnection(url, username, passwd);
			System.out.println("Connexion a la base de donnée établie.");

			PreparedStatement st = ct.prepareStatement("SELECT * FROM recette;");
			ResultSet result = st.executeQuery();
			while(result.next()) {
				System.out.println(result.getString("nom"));
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		var mav = new ModelAndView("index");

		mav.addObject("username", "Noemie");
		mav.setViewName("index");

		return mav;
	}
}
