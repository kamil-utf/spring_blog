package com.example.blog.controller;

import com.example.blog.config.WebMvcConfig;
import com.example.blog.model.User;
import com.example.blog.service.AuthorityService;
import com.example.blog.service.UserService;
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
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthorityService authorityService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                    .setControllerAdvice(new ExceptionController()).build();
    }

    @Test
    public void browseShouldRetrieveListOfUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get(UserController.ADMIN_PREFIX))
                .andExpect(status().isOk())
                .andExpect(view().name("user/browse"))
                .andExpect(model().attribute("users", hasSize(2)));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void editShouldGetUserEditFormIfExistingID() throws Exception {
        User user = createUserWithExistingID();

        when(userService.findById(user.getId())).thenReturn(user);

        mockMvc.perform(get(UserController.ADMIN_PREFIX + "/{userId}/edit", user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attribute("user", instanceOf(User.class)));

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void editShouldBeUserNotFoundIfNonExistingID() throws Exception {
        User user = createUserWithNonExistingID();

        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(get(UserController.ADMIN_PREFIX + "/{userId}/edit", user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void removeShouldDeletePostIfExistingID() throws Exception {
        User user = createUserWithExistingID();

        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(userService).delete(user);

        mockMvc.perform(delete(UserController.ADMIN_PREFIX + "/{userId}", user.getId()))
                .andExpect(redirectedUrl(UserController.ADMIN_PREFIX));

        verify(userService, times(1)).findById(user.getId());
        verify(userService, times(1)).delete(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void removeShouldBeUserNotFoundIfNonExistingID() throws Exception {
        User user = createUserWithNonExistingID();

        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(delete(UserController.ADMIN_PREFIX + "/{userId}", user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void resetShouldSetDefaultPasswordIfExistingID() throws Exception {
        User user = createUserWithExistingID();

        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(userService).resetPassword(user);

        mockMvc.perform(post(UserController.ADMIN_PREFIX + "/{userId}/passwd", user.getId()))
                .andExpect(redirectedUrl(UserController.ADMIN_PREFIX));

        verify(userService, times(1)).findById(user.getId());
        verify(userService, times(1)).resetPassword(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void resetShouldBeUserNotFoundIfNonExistingID() throws Exception {
        User user = createUserWithNonExistingID();

        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(post(UserController.ADMIN_PREFIX + "/{userId}/passwd", user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userService);
    }

    private User createUserWithExistingID() {
        User user = new User();
        user.setId(1L);

        return user;
    }

    private User createUserWithNonExistingID() {
        User user = new User();
        user.setId(Long.MAX_VALUE);

        return user;
    }
}
