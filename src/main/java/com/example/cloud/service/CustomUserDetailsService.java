package com.example.cloud.service;

import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.cloud.util.CloudLogger.logInfo;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if(!userRepository.existsUserByUsername(username)) {
         throw new UsernameNotFoundException("User " + username + " not found");
      }

      logInfo("Getting user from DB...");
      User user = userRepository.getUserByUsername(username);
      return org.springframework.security.core.userdetails.User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .authorities(user.getAuthorities())
              .build();
   }

}