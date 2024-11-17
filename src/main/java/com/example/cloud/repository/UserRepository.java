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

   User getUserById(long id);

   User getUserByToken(String token);

   Boolean existsUserByUsername(String login);

   int getUserIdByToken(String token);

   @Query("SELECT u.password FROM User u WHERE u.username = ?1")
   String getPasswordByUsername(String username);

   @Query("SELECT u.token FROM User u WHERE u.username = ?1")
   String getTokenByUsername(String username);

   @Modifying
   @Query("UPDATE User u SET u.token = :token WHERE u.username = :username")
   void updateTokenByUsername(String token, String username);

   @Modifying
   @Transactional
   @Query("UPDATE User u SET u.token = null WHERE u.username = :username")
   void deleteTokenByUsername(String username);

}