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
@RequestMapping(UserController.ADMIN_PREFIX)
public class UserController {

    public static final String ADMIN_PREFIX = "/admin/users";

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
        User user = userService.findOne(userId);
        if(user == null) {
            throw new ResourceNotFoundException("User with id " + userId + " not found!");
        }

        model.addAttribute("user", user);
        return "user/edit";
    }

    @PutMapping("/{userId}")
    public String updateUser(@Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            return "user/edit";
        }

        userService.update(user);
        return "redirect:" + ADMIN_PREFIX;
    }

    @DeleteMapping("/{userId}")
    public String removeUser(@PathVariable Long userId) {
        User user = userService.findOne(userId);

        userService.delete(user);
        return "redirect:" + ADMIN_PREFIX;
    }

    @PostMapping("/{userId}/passwd")
    public String resetUserPassword(@PathVariable Long userId) {
        User user = userService.findOne(userId);

        userService.resetPassword(user);
        return "redirect:" + ADMIN_PREFIX;
    }
}