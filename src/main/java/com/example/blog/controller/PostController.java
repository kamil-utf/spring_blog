package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PostController {

	private static final String WRITER_PREFIX = "/writer/posts";

	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping(WRITER_PREFIX)
	public String browsePosts(Model model) {
		model.addAttribute("posts", postService.findAll());
		return "post/browse";
	}

	@GetMapping(WRITER_PREFIX + "/create")
	public String createPost(Model model) {
		model.addAttribute("post", new Post());
		return "post/createOrEdit";
	}

	@PostMapping(WRITER_PREFIX)
	public String storePost(@Valid Post post, BindingResult result) {
		if(result.hasErrors()) {
			return "post/createOrEdit";
		}

		postService.save(post);
		return "redirect:" + WRITER_PREFIX;
	}

	@DeleteMapping(WRITER_PREFIX + "/{postId}")
	public String removePost(@PathVariable Long postId) {
		Post post = postService.findById(postId);

		postService.delete(post);
		return "redirect:" + WRITER_PREFIX;
	}
}
