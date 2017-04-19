package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findOne(final Long id) {
        return postRepository.findOne(id);
    }

    @Override
    public void saveOrUpdate(final Post post) {
        Long postId = post.getId();
        Post existing = postId != null ? findOne(postId) : null;
        if(existing != null) {
            post.setAuthor(existing.getAuthor());
            post.setCreatedDate(existing.getCreatedDate());
        }

        postRepository.save(post);
    }

    @Override
    public void delete(final Post post) {
        postRepository.delete(post);
    }
}
