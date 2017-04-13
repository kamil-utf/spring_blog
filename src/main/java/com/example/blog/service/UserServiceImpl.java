package com.example.blog.service;

import com.example.blog.exception.IllegalOperationException;
import com.example.blog.model.Authority;
import com.example.blog.model.User;
import com.example.blog.repository.AuthorityRepository;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final boolean INSTANT_ACCOUNT_ACTIVATION = true;
    private static final String  DEFAULT_PASSWORD = "12345678";

    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean PASSWORD_NON_EXPIRED = true;
    private static final boolean ACCOUNT_NON_LOCKED = true;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthorityRepository authorityRepository,
                           @Lazy PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(INSTANT_ACCOUNT_ACTIVATION);

        // By default new members are assigned to Role.USER group
        Authority userAuth = authorityRepository.findByRole(Authority.Role.USER);
        user.setAuthorities(new HashSet<>(Arrays.asList(userAuth)));

        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        User oldUser = userRepository.findById(user.getId());
        Long numberOfAdmins = userRepository.countByAuthorities_Role(Authority.Role.ADMIN);

        // Check if the system has at least one administrator
        if(!isAdmin(user) && isAdmin(oldUser) && numberOfAdmins < 2) {
            throw new IllegalOperationException("System must have at least one administrator!");
        }

        user.setPassword(
                user.getPassword() != null
                ? passwordEncoder.encode(DEFAULT_PASSWORD)
                : oldUser.getPassword()
        );

        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }

        Collection<? extends GrantedAuthority> authorities = convertAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(),
                ACCOUNT_NON_EXPIRED, PASSWORD_NON_EXPIRED, ACCOUNT_NON_LOCKED, authorities
        );
    }

    private Collection<? extends GrantedAuthority> convertAuthorities(User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Authority authority : user.getAuthorities()) {
            grantedAuthorities.add(
                    new SimpleGrantedAuthority(authority.getRole().getFullName())
            );
        }

        return grantedAuthorities;
    }

    private boolean isAdmin(User user) {
        Set<Authority> authorities = user.getAuthorities();
        return authorities.stream()
                .anyMatch(auth -> auth.getRole().equals(Authority.Role.ADMIN));
    }
}
