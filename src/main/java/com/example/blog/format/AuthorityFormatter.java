package com.example.blog.format;

import com.example.blog.model.Authority;
import com.example.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class AuthorityFormatter implements Formatter<Authority> {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityFormatter(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public String print(Authority authority, Locale locale) {
        return authority.getId().toString();
    }

    @Override
    public Authority parse(String formatted, Locale locale) throws ParseException {
        Long id = Long.parseLong(formatted);
        return authorityService.findById(id);
    }
}
