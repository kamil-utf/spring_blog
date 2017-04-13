package com.example.blog.controller;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Authority;
import com.example.blog.model.User;
import com.example.blog.service.AuthorityService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private static final String ADMIN_PREFIX = "/admin/users";

    private final UserService userService;
    private final AuthorityService authorityService;

    @Autowired
    public UserController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @ModelAttribute("allAuthorities")
    public List<Authority> authorities() {
        return authorityService.findAll();
    }

    @GetMapping
    public String browseUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/browse";
    }

    @GetMapping("/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model) throws ResourceNotFoundException {
        User user = userService.findById(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found!");
        }

        model.addAttribute("user", user);
        return "user/edit";
    }

    @PutMapping("/{userId}")
    public String updateUser(@Valid User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "user/edit";
        }

        userService.update(user);
        return "redirect:" + ADMIN_PREFIX;
    }

    @DeleteMapping("/{userId}")
    public String removeUser(@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:" + ADMIN_PREFIX;
    }
}