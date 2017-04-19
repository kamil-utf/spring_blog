package com.example.blog.service;

import com.example.blog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findOne(Long id);
    void save(User user);
    void update(User user);
    void delete(User user);
    void resetPassword(User user);
}