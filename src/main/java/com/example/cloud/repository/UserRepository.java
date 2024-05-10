package com.example.cloud.repository;

import com.example.cloud.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

   User save(User user);

   User getUserByLogin(String login);

   User getUserById(long id);

}