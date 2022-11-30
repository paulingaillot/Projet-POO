package com.poo.projetfinal;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Admin {

    @GetMapping("/admin")
	public ModelAndView profil(HttpServletRequest request) {
        var mav = new ModelAndView("admin");

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

}
