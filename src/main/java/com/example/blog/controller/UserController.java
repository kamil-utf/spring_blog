package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView browseUsers() {
        List<User> users = userService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin.browseUsers");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/{userId}/edit")
    public ModelAndView editUser(@PathVariable Long userId) {
        User user = userService.findById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin.editUser");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}