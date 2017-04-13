package com.example.blog.service;


import com.example.blog.model.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> findAll();
    Authority findById(Long id);
}
