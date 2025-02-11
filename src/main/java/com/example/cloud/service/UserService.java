package com.example.cloud.service;

import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import com.example.cloud.repository.UserTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
   private final UserRepository userRepository;
   private final UserTokenRepository userTokenRepository;

   public String getPasswordByUsername(String username) {
      String password = userRepository.getPasswordByUsername(username);
      if (password == null) {
         throw new RuntimeException("User not found or password is null");
      }
      return password;
   }

   public void mapTokenToUser(String token, User user) {
      userTokenRepository.putTokenAndUser(token, user);
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepository.getUserByUsername(username);
   }
}