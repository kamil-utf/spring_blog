package com.example.blog.model;

import com.example.blog.util.AuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserAuditDetails implements UserDetails {

    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean ACCOUNT_NON_LOCKED = true;
    private static final boolean PASSWORD_NON_EXPIRED = true;

    private final User user;

    public UserAuditDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.convertAuthorityList(user);
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return ACCOUNT_NON_EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ACCOUNT_NON_LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return PASSWORD_NON_EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return user;
    }
}
