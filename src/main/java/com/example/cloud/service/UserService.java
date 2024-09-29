package com.example.cloud.service;

import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   // ======= METHODS =======

   public User save(User user) {
      return userRepository.save(user);
   }

   public User getUserByLogin(String login) {
      return userRepository.getUserByUsername(login);
   }

   public User getUserById(long id) {
      return userRepository.getUserById(id);
   }

   public Boolean existsUserByUsername(String login) {
      return userRepository.existsUserByUsername(login);
   }

   public void updateTokenByUsername(String authToken, String username) {
      userRepository.updateTokenByUsername(authToken, username);
   }

   public void deleteTokenByUsername(String username) {
      userRepository.deleteTokenByUsername(username);
   }

   public int getUserIdByToken(String token) {
      return userRepository.getUserIdByToken(token);
   }

}