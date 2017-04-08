package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String browseUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/browse";
    }

    @DeleteMapping("/{userId}/delete")
    public String removeUser(@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:/admin/users";
    }
}