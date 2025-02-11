package com.example.cloud.repository;

import com.example.cloud.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

   User save(User user);

   User getUserByUsername(String login);

   Boolean existsUserByUsername(String login);

   @Query("SELECT u.password FROM User u WHERE u.username = ?1")
   String getPasswordByUsername(String username);

}