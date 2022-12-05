package com.poo.projetfinal.config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.poo.projetfinal.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringBootSessionController {

    @RequestMapping(value = "/addNote", method = {RequestMethod.POST, RequestMethod.GET})
    public String addNote(@RequestParam("note") String note,
                          @Nullable @RequestParam("redirectPage") String redirect ,
                          @RequestParam("key") String sessionKey ,
                          HttpServletRequest request) {
        System.out.println(" defining "+ sessionKey +" : |" + note +"|");

        @SuppressWarnings("unchecked")
        List<String> notes = (List<String>) request.getSession().getAttribute("NOTES_SESSION");
        if (notes == null) {
            notes = new ArrayList<>();
            // if notes object is not present in session, set notes in the request session
            request.getSession().setAttribute(sessionKey, notes);
        }
        notes.add(note);
        request.getSession().setAttribute(sessionKey, notes);

        System.out.println("contenu de la session");
        for (String _note: notes) {
            System.out.println("item : |" + _note +"|");
        }
        return "redirect:" + redirect;
    }

    @GetMapping("/debug/session")
    public ModelAndView displaySession(Model model, HttpSession session) {
//        List<String> notes = (List<String>) session.getAttribute("NOTES_SESSION");
//        notes = notes!=null? notes:new ArrayList<>();
        List<String> notes = new ArrayList<>();
        Enumeration<String> list = session.getAttributeNames();

        for (Iterator<String> it = list.asIterator(); it.hasNext(); ) {
            String item = it.next();
            @SuppressWarnings("unchecked")
            List<String> secondItem = (List<String>) session.getAttribute(item);
            System.out.println(item + " : " + secondItem);
            notes.add(String.join(", " , secondItem));

        }

        model.addAttribute("notesSession", notes);

        var mav = new ModelAndView("debugsession");
        mav.addObject("blocData" , notes);
        return mav;
    }

    @PostMapping("/invalidate/session")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/debug/session";
    }

    public static String generateUniqueID(User user) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest((user.getPrenom()+user.getNom()).getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}