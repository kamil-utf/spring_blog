package com.example.blog.controller;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    public static final int PAGE_SIZE = 2;

    private static final int PAGE_INIT = 1;
    private static final int PAGE_SHIFT = 3;

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public BlogController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping({"/", "/posts", "/posts/{pageNumber}"})
    public String index(@PathVariable Optional<Integer> pageNumber, Model model) {
        Page<Post> page = postService.findAll(pageNumber.orElse(PAGE_INIT));

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - PAGE_SHIFT);
        int end = Math.min(current + PAGE_SHIFT, page.getTotalPages());

        model.addAttribute("posts", page);
        model.addAttribute("current", current);
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);
        return "index";
    }

    @GetMapping("/posts/{postId}/details")
    public String showPost(@PathVariable Long postId, Model model) throws ResourceNotFoundException {
        Post post = postService.findOne(postId);
        if(post == null) {
            throw new ResourceNotFoundException("Post with id " + postId + " not found!");
        }

        model.addAttribute("post", post);
        return "post/details";
    }

    @JsonView(Post.View.class)
    @GetMapping("/posts/autocomplete")
    public @ResponseBody List<Post> autoComplete(@RequestParam("term") String query) {
        return postService.findByTitleContaining(query);
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        return principal == null ? "login" : "redirect:/";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Validated(User.PasswordChecks.class) User user, BindingResult result) {
        if(result.hasErrors()) {
            return "register";
        }

        userService.save(user);
        return "redirect:/login";
    }
}
