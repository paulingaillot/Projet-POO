package com.poo.projetfinal.Config;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringBootSessionController {

    @RequestMapping(value = "/addToSession", method = {RequestMethod.POST, RequestMethod.GET})
    public String addNote(@RequestParam("note") String note,
                          @Nullable @RequestParam("redirectPage") String redirect ,
                          @RequestParam("key") String sessionKey ,
                          HttpServletRequest request) {
                            
        System.out.println(" defining "+ sessionKey +" : |" + note +"|");

        request.getSession().setAttribute(sessionKey, note);

        System.out.println("contenu de la session");

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

}