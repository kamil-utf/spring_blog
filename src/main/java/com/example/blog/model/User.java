package com.example.blog.model;

import com.example.blog.validation.Matches;
import com.example.blog.validation.Unique;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.util.Set;

@Entity
@Table(name = "users")
@DynamicUpdate
@Unique(property = "username")
@Matches(property = "password", verifier = "confirmPassword", groups = User.PasswordChecks.class)
public class User {

    public interface PasswordChecks extends Default {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 25)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotNull(groups = PasswordChecks.class)
    @Size(min = 6, max = 20, groups = PasswordChecks.class)
    private String password;

    @Transient
    private String confirmPassword;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_authorities",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
