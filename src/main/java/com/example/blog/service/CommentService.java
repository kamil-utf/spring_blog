package com.example.blog.service;

import com.example.blog.model.Comment;

public interface CommentService {
    Comment findById(Long id);
    void save(Comment comment);
}
