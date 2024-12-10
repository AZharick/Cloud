package com.example.cloud.service;

import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
   private final UserRepository userRepository;

   public User getUserByToken(String token) {
      return userRepository.getUserByToken(token);
   }

   public String getPasswordByUsername(String username) {
      String password = userRepository.getPasswordByUsername(username);
      if (password == null) {
         throw new RuntimeException("User not found or password is null");
      }
      return password;
   }

   public String getTokenByUsername(String username) {
      String token = userRepository.getTokenByUsername(username);
      if (token == null) {
         throw new RuntimeException("User not found or token is null");
      }
      return token;
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