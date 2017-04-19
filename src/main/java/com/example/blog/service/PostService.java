package com.example.blog.service;

import com.example.blog.model.Post;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    Post findOne(Long id);
    void saveOrUpdate(Post post);
    void delete(Post post);
}
