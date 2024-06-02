package com.example.cloud.repository;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
   Authority findById(int id);
   Authority findByUsers(Set<User> users);
}