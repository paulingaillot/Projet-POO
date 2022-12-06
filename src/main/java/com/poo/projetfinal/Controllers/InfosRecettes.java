package com.poo.projetfinal.Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.poo.projetfinal.Database;

@RestController
public class InfosRecettes {

    @GetMapping("/recette")
    public ModelAndView Index(HttpServletRequest request, int id_recette) throws SQLException {

        Database sql = new Database();
        ResultSet recette = sql.getRecette("" + id_recette);
        recette.next();

        var mav = new ModelAndView("recette");

        String imagevalue = "";
        try {
            byte[] imagetab = sql.chargeIMG(id_recette + "");
            String response = Base64.getEncoder().encodeToString(imagetab);
            imagevalue = "<img width=100% src='data:image/png;base64," + response + "'></img>";
        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.addObject("recette_image", imagevalue);

        mav.addObject("recette_title", recette.getString("nom"));
        mav.addObject("recette_cout", recette.getString("budget"));
        mav.addObject("recette_duree", recette.getString("temps"));

        String ingredient1 = recette.getString("ingredients");
        String result = "";
        if (ingredient1 != null) {
            String[] ingredients = ingredient1.split("\n");
            result = "<ul>";
            for (String ingredient : ingredients) {
                result += "<li>" + ingredient + "</li>";
            }
            result += "</ul>";
        }

        mav.addObject("recette_ingredients", result);

        String prepa1 = recette.getString("prepa");
        result = "";
        if (prepa1 != null) {
            String[] prepas = prepa1.split("\n");
            result = "<ul>";
            for (String prepa : prepas) {
                result += "<li>" + prepa + "</li>";
            }
            result += "</ul>";
        }

        mav.addObject("recette_prepa", result);

        sql.close();

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
