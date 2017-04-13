package com.example.blog.controller;

import com.example.blog.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public void resourceNotFound(HttpServletResponse response, ResourceNotFoundException ex) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
    }
}
