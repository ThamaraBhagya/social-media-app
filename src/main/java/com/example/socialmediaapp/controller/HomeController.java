package com.example.socialmediaapp.controller;

import com.example.socialmediaapp.dto.PostDto;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal User currentUser, Model model) {
        List<PostDto> posts = postService.getPostsForUserFeed(currentUser.getId());
        model.addAttribute("posts", posts);
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("currentUser", currentUser);
        return "home";
    }

    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("postDto") PostDto postDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User currentUser,
                             Model model) {
        if (bindingResult.hasErrors()) {
            List<PostDto> posts = postService.getPostsForUserFeed(currentUser.getId());
            model.addAttribute("posts", posts);
            model.addAttribute("currentUser", currentUser);
            return "home";
        }

        postService.createPost(postDto, currentUser);
        return "redirect:/home";
    }
}