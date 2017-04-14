package com.example.blog.service;

import com.example.blog.exception.IllegalOperationException;
import com.example.blog.model.Authority;
import com.example.blog.model.User;
import com.example.blog.model.UserAuditDetails;
import com.example.blog.repository.AuthorityRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.util.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final boolean INSTANT_ACCOUNT_ACTIVATION = true;
    private static final String  DEFAULT_PASSWORD = "12345678";

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

        // Check if the system has at least one administrator
        if(!AuthorityUtils.isAdmin(user) && AuthorityUtils.isAdmin(oldUser) && countAdmins() < 2) {
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
        User user = userRepository.findById(id);

        // Check if the system has at least one administrator
        if(AuthorityUtils.isAdmin(user) && countAdmins() < 2) {
            throw new IllegalOperationException("System must have at least one administrator!");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }

        return new UserAuditDetails(user);
    }

    private Long countAdmins() {
        return userRepository.countByAuthorities_Role(Authority.Role.ADMIN);
    }
}
