package com.example.blog.util;

import com.example.blog.model.Authority;
import com.example.blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthorityUtils {

    public static Collection<? extends GrantedAuthority> convertAuthorityList(User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Authority authority : user.getAuthorities()) {
            grantedAuthorities.add(
                    new SimpleGrantedAuthority(authority.getRole().getFullName())
            );
        }

        return grantedAuthorities;
    }

    public static boolean isAdmin(User user) {
        Set<Authority> authorities = user.getAuthorities();
        return authorities.stream()
                .anyMatch(auth -> auth.getRole().equals(Authority.Role.ADMIN));
    }
}
