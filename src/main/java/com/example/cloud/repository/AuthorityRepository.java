package com.example.cloud.repository;

import com.example.cloud.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
   Authority findById(int id);
}