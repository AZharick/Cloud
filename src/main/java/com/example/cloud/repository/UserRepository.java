package com.example.cloud.repository;

import com.example.cloud.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

   User save(User user);

   User getUserByUsername(String login);  //Optional recommended by Teddy

   User getUserById(long id);     //Optional recommended by Teddy

   Boolean existsUserByUsername(String login);    //recommended by Teddy

}