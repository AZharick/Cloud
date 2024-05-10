package com.example.cloud.service;

import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   private final UserRepository userRepository;

   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   User save(User user) {
      return userRepository.save(user);
   }

   User getUserByLogin(String login) {
      return userRepository.getUserByLogin(login);
   }

   User getUserById(long id) {
      return userRepository.getUserById(id);
   }

}
