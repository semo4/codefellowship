package com.example.CodeFellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller

public class GeneralController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @GetMapping
    public String getHomePage(Principal p, Model m){
        if(p!=null){
            ApplicationUser user = applicationUserRepository.findByUsername(p.getName());
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
            m.addAttribute("isLogin","true");
//            m.addAttribute("username", p.getName());
            return "homepage.html";
        }
        m.addAttribute("isLogin","false");
        return "homepage.html";
    }
}
