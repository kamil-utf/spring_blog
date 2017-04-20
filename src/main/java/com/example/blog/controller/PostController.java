package com.example.blog.controller;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import com.example.blog.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping(PostController.WRITER_PREFIX)
public class PostController {

	public static final String WRITER_PREFIX = "/writer/posts";

	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public String browsePosts(Model model) {
		model.addAttribute("posts", postService.findAll());
		return "post/browse";
	}

	@GetMapping("/create")
	public String createPost(Model model) {
		model.addAttribute("post", new Post());
		return "post/createOrEdit";
	}

	@GetMapping("/{postId}/edit")
	public String editPost(@PathVariable Long postId, Model model) throws ResourceNotFoundException {
		Post post = postService.findOne(postId);
		if(post == null) {
			throw new ResourceNotFoundException("Post with id " + postId + " not found!");
		}

		model.addAttribute("post", post);
		return "post/createOrEdit";
	}

	@PostMapping
	public String storePost(@Valid Post post, BindingResult result,
							@RequestParam(value = "file", required = false) MultipartFile file) {
		if(result.hasErrors()) {
			return "post/createOrEdit";
		}

		if(!file.isEmpty()) {
			try {
				post.setImage(ImageUtils.createImageForPost(file));
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}

		postService.saveOrUpdate(post);
		return "redirect:" + WRITER_PREFIX;
	}

	@DeleteMapping("/{postId}")
	public String removePost(@PathVariable Long postId) {
		Post post = postService.findOne(postId);

		postService.delete(post);
		return "redirect:" + WRITER_PREFIX;
	}
}
