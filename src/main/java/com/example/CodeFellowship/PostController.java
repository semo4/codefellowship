package com.example.CodeFellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class PostController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/post")
    public String postPage(Principal p,  Model m){
        if(p!=null){
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
            m.addAttribute("userId", applicationUser.getId());
            return "post.html";
        }
        return "error.html";
    }
    @PostMapping("/post")
    public RedirectView getPostData(int userid, String body ){
        ApplicationUser applicationUser = applicationUserRepository.findById(userid).get();
        PostModel posts = new PostModel(body, applicationUser);
        postRepository.save(posts);
        return new RedirectView("/profile");

    }



}
