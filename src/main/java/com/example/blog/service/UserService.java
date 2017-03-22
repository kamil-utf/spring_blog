package com.example.blog.service;

import com.example.blog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
}