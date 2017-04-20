package com.example.blog.service;

import com.example.blog.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    Page<Post> findAll(Integer pageNumber);
    Post findOne(Long id);
    List<Post> findByTitleContaining(String title);
    void saveOrUpdate(Post post);
    void delete(Post post);
}
