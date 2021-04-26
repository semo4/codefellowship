package com.example.CodeFellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    PostRepository postRepository;





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

    @GetMapping("/profile")
    public String getUserProfilePage(Principal p,Model m){
        if(p!= null){
            ApplicationUser user = applicationUserRepository.findByUsername(p.getName());
            List<PostModel> posts =  postRepository.findPostByUserId(user.getId());
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
            m.addAttribute("posts", posts);
            m.addAttribute("username", p.getName());
            return  "profile.html";
        }
        return "error.html";

    }

//    @GetMapping("/profile")
//    public String gatProfile( Model m, Principal p){
//        m.addAttribute("user",((UsernamePasswordAuthenticationToken)p).getPrincipal() );
//        ApplicationUser user = applicationUserRepository.findByUsername(p.getName());
//        List<PostModel> posts =  postRepository.findById(user.getId());
//        m.addAttribute("user", user);
//        m.addAttribute("posts", posts);
//        m.addAttribute("username", p.getName());
//        return "profile.html";
//    }
//
    @GetMapping("/profile/{id}")
    public String getProfileById(@PathVariable int id, Principal p , Model m){
        ApplicationUser user = applicationUserRepository.findByUsername(p.getName());
        List<PostModel> posts =  postRepository.findPostByUserId(id);
        m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        m.addAttribute("posts", posts);
        m.addAttribute("username", p.getName());
        return "profile.html";
    }

    @GetMapping("/logout")
    public RedirectView getLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new RedirectView("/");
    }



}
