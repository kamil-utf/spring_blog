package com.example.blog.repository;

import com.example.blog.model.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Authority findByRole(Authority.Role role);
}
