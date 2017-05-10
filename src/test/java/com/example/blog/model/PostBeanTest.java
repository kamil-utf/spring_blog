package com.example.blog.model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.*;

public class PostBeanTest {

    private static final String TITLE_EXAMPLE = "Post Title";
    private static final String CONTENT_EXAMPLE = "Post Content";

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void shouldBeValid() {
        Post post = new Post();
        post.setTitle(TITLE_EXAMPLE);
        post.setContent(CONTENT_EXAMPLE);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldNotBeValidWhenTitleIsEmpty() {
        Post post = new Post();
        post.setTitle("");
        post.setContent(CONTENT_EXAMPLE);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotBeValidWhenTitleLengthIsLessThan5() {
        Post post = new Post();
        post.setTitle("Test");
        post.setContent(CONTENT_EXAMPLE);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotBeValidWhenContentIsEmpty() {
        Post post = new Post();
        post.setTitle(TITLE_EXAMPLE);
        post.setContent("");

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertFalse(violations.isEmpty());
    }
}
