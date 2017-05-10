package com.example.blog.controller;

import com.example.blog.config.WebMvcConfig;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
public class BlogControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private BlogController blogController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // It deals with circular view path Exception
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                    .standaloneSetup(blogController)
                    .setViewResolvers(viewResolver)
                    .setControllerAdvice(new ExceptionController()).build();
    }

    @Test
    public void indexShouldRetrieveListOfPostsWithPagination() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        Integer pageNumber = 1;
        when(postService.findAll(pageNumber)).thenReturn(new PageImpl<>(posts));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("posts", instanceOf(Page.class)))
                .andExpect(model().attribute("current", instanceOf(Integer.class)))
                .andExpect(model().attribute("begin", instanceOf(Integer.class)))
                .andExpect(model().attribute("end", instanceOf(Integer.class)));

        verify(postService, times(1)).findAll(pageNumber);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void showShouldRetrievePostDetailsIfExistingID() throws Exception {
        Post post = PostControllerTest.createPostWithExistingID();

        when(postService.findById(post.getId())).thenReturn(post);

        mockMvc.perform(get("/posts/{postId}/details", post.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post/details"))
                .andExpect(model().attribute("post", post));

        verify(postService, times(1)).findById(post.getId());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void autoCompleteShouldReturnPostsThatContainSpecificTitle() throws Exception {
        Post post = PostControllerTest.createPostWithNonExistingID();

        String term = "hello";
        when(postService.findByTitleContaining(term)).thenReturn(Arrays.asList(post));

        mockMvc.perform(get("/posts/autocomplete").param("term", term))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(postService, times(1)).findByTitleContaining(term);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void loadShouldGetPostImageIfExistingID() throws Exception {
        Post post = PostControllerTest.createPostWithExistingID();

        when(postService.findById(post.getId())).thenReturn(post);

        mockMvc.perform(get("/posts/{postId}/image", post.getId()))
                .andExpect(status().isOk());

        verify(postService, times(1)).findById(post.getId());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void loginShouldGetSignInForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void loginShouldRedirectWhenUserIsLoggedIn() throws Exception {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(new User(), null);

        mockMvc.perform(get("/login").principal(auth))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void registerShouldGetSignUpForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("user", instanceOf(User.class)));
    }
}
