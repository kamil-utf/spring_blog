package com.example.blog.controller;

import com.example.blog.config.WebMvcConfig;
import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                    .setControllerAdvice(new ExceptionController()).build();
    }

    @Test
    public void browseShouldRetrieveListOfPosts() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        when(postService.findAll()).thenReturn(posts);

        mockMvc.perform(get(PostController.WRITER_PREFIX))
                .andExpect(status().isOk())
                .andExpect(view().name("post/browse"))
                .andExpect(model().attribute("posts", hasSize(2)));

        verify(postService, times(1)).findAll();
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void createShouldGetPostCreateForm() throws Exception {
        mockMvc.perform(get(PostController.WRITER_PREFIX + "/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/createOrEdit"))
                .andExpect(model().attribute("post", instanceOf(Post.class)));
    }

    @Test
    public void editShouldGetPostEditFormIfExistingID() throws Exception {
        Post post = createPostWithExistingID();

        when(postService.findById(post.getId())).thenReturn(post);

        mockMvc.perform(get(PostController.WRITER_PREFIX + "/{postId}/edit", post.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post/createOrEdit"))
                .andExpect(model().attribute("post", instanceOf(Post.class)));

        verify(postService, times(1)).findById(post.getId());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void editShouldBePostNotFoundIfNonExistingID() throws Exception {
        Post post = createPostWithNonExistingID();

        when(postService.findById(post.getId())).thenReturn(null);

        mockMvc.perform(get(PostController.WRITER_PREFIX + "/{postId}/edit", post.getId()))
                .andExpect(status().isNotFound());

        verify(postService, times(1)).findById(post.getId());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void storeShouldSaveOrUpdatePost() throws Exception {
        doNothing().when(postService).saveOrUpdate(any(Post.class));

        mockMvc.perform(post(PostController.WRITER_PREFIX))
                .andExpect(redirectedUrl(PostController.WRITER_PREFIX));

        verify(postService, times(1)).saveOrUpdate(any(Post.class));
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void removeShouldDeletePostIfExistingID() throws Exception {
        Post post = createPostWithExistingID();

        when(postService.findById(post.getId())).thenReturn(post);
        doNothing().when(postService).delete(post);

        mockMvc.perform(delete(PostController.WRITER_PREFIX + "/{postId}", post.getId()))
                .andExpect(redirectedUrl(PostController.WRITER_PREFIX));

        verify(postService, times(1)).findById(post.getId());
        verify(postService, times(1)).delete(post);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void removeShouldBePostNotFoundIfNonExistingID() throws Exception {
        Post post = createPostWithNonExistingID();

        when(postService.findById(post.getId())).thenReturn(null);

        mockMvc.perform(delete(PostController.WRITER_PREFIX + "/{postId}", post.getId()))
                .andExpect(status().isNotFound());

        verify(postService, times(1)).findById(post.getId());
        verifyNoMoreInteractions(postService);
    }

    private Post createPostWithExistingID() {
        Post post = new Post();
        post.setId(1L);

        return post;
    }

    private Post createPostWithNonExistingID() {
        Post post = new Post();
        post.setId(Long.MAX_VALUE);

        return post;
    }
}
