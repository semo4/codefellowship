package com.example.CodeFellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String getStringPage(Principal p , Model m){
        if(p!=null){
            m.addAttribute("isLogin","true");
            m.addAttribute("user", p.getName());
            return "homepage.html";
        }
        m.addAttribute("isLogin","false");
        return "signup.html";
    }


    @GetMapping("/login")
    public String getLogin( Model m){
        m.addAttribute("isLogin","false");
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
//            m.addAttribute("username", p.getName());
            m.addAttribute("isLogin","true");
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
        if(p!= null){
            ApplicationUser user = applicationUserRepository.findById(id).get();
            ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
            List<PostModel> posts =  postRepository.findPostByUserId(id);
            m.addAttribute("prouser", user);
            m.addAttribute("user", currentUser);
            m.addAttribute("posts", posts);
//            m.addAttribute("username", p.getName());
            m.addAttribute("isLoginUser","true");
            m.addAttribute("isLogin","true");

            return  "my-profile.html";
        }
        return "error.html";
    }

    @GetMapping("/logout")
    public RedirectView getLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new RedirectView("/");
    }

    @GetMapping("/edit")
    public String getEdit(Principal p,Model m){
        if(p!= null){
            boolean isAllowedToEdit = true;
            m.addAttribute("isAllowedToEdit",isAllowedToEdit );
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
            m.addAttribute("isLogin","true");
            return "edit.html";
        }
        return "error.html";
    }

    @PutMapping("/edit/{id}")
    public RedirectView updateProfile(Principal p, @PathVariable int id,@RequestParam(value="username") String username,
                                @RequestParam(value="firstname") String firstname,
                                @RequestParam(value="lastname") String lastname,
                                @RequestParam(value="bio") String bio){
        ApplicationUser updateUser = applicationUserRepository.findByUsername(p.getName());
        if(updateUser.getId() == id){
            updateUser.setUsername(username);
            updateUser.setFirstName(firstname);
            updateUser.setLastName(lastname);
            updateUser.setBio(bio);
            applicationUserRepository.save(updateUser);
            return new RedirectView("/profile/"+id);
        }
        return new RedirectView("/error");

    }

    @GetMapping("/error")
    public String getError(){
        return "error.html";
    }


    @GetMapping("/users")
    public String gatAllUsersPage(Principal p, Model m){
        if(p!= null){
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        }
        List<ApplicationUser> users = applicationUserRepository.findAll();
        ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
        users.remove(currentUser);
        m.addAttribute("users", users);
        m.addAttribute("isLogin","true");
        return "allusers.html";
    }

    @PostMapping("/follow")
    public RedirectView addUserIFollow(@Param("id") int id, Principal p){
        ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
        ApplicationUser userToFollow = applicationUserRepository.getOne(id);
        currentUser.addUserToFollow(userToFollow);
        applicationUserRepository.save(currentUser);
        return new RedirectView("/feed");
    }

    @GetMapping("/feed")
    public String getMyFeedPage(Model m, Principal p){
        if(p!= null){
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        }
        ApplicationUser currentUser = applicationUserRepository.findByUsername(p.getName());
        m.addAttribute("currentUser", currentUser);
        m.addAttribute("isLogin","true");
        return "feed.html";
    }


}
