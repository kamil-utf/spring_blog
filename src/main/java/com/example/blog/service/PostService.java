package com.example.blog.service;

import com.example.blog.model.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    void save(Post post);
}
