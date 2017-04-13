package com.example.blog.repository;

import com.example.blog.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findById(Long id);
    Authority findByRole(Authority.Role role);
}
