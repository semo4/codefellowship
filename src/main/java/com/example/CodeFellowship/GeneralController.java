package com.example.CodeFellowship;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller

public class GeneralController {

    @GetMapping
    public String getHomePage(Principal p, Model m){
        if(p!=null){
            m.addAttribute("username", p.getName());
        }
        return "homepage.html";
    }
}
