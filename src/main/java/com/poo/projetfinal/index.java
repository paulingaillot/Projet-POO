package com.poo.projetfinal;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;

import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class index {
	@GetMapping("/")
	public ModelAndView index() {

		String url = "jdbc:mysql://127.0.0.1:3306";
		String port = "3308";
		String username = "new_user";
		String passwd = "test";

		try {
			Connection ct = DriverManager.getConnection(url, username, passwd);
			System.out.println("Connexion a la base de donnée établie.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		var mav = new ModelAndView("index");

		mav.addObject("username", "Noemie");
		mav.setViewName("index");

		return mav;
	}

}
