package com.example.CodeFellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    @GetMapping("/signup")
    public String getStringPage(){
        return "signup.html";
    }


    @GetMapping("/login")
    public String getLogin(){
        return "login.html";
    }

    @PostMapping("/signup")
    public RedirectView signup(@RequestParam(value="username") String username,
                               @RequestParam(value="password") String password,
                               @RequestParam(value="firstname") String firstname,
                               @RequestParam(value="lastname") String lastname,
                               @RequestParam(value="date") String date,
                               @RequestParam(value="bio") String bio){
        ApplicationUser newUser = new ApplicationUser(username,bCryptPasswordEncoder.encode(password),firstname,lastname,date,bio);
        newUser = applicationUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/login");
    }

    @PostMapping("/perform_login")
    public String getUserProfilePage(Principal p,Model m){
        m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        return "profile.html";
    }

//    @PostMapping("/perform_login")
//    public RedirectView getUserProfile(@RequestParam(value="username") String username,
//                                       @RequestParam(value="password") String password){
//        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
//
//        if(bCryptPasswordEncoder.matches(password,applicationUser.getPassword())) {
//            return new RedirectView("/profile");
//        }
//        return new RedirectView("/perform_login");
//    }
//
    @GetMapping("/profile")
    public String gatProfile( Model m, Principal p){
        if(p!=null){
            m.addAttribute("username", p.getName());
        }
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
        m.addAttribute("user",applicationUser );
        return "profile.html";

    }





}
