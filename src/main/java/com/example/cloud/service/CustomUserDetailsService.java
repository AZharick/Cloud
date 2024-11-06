package com.example.cloud.service;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

   private UserRepository userRepository;

   @Autowired
   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if(!userRepository.existsUserByUsername(username)) {
         throw new UsernameNotFoundException("User " + username + " not found");
      }
      User user = userRepository.getUserByUsername(username);

      return (UserDetails) User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .authorities(user.getAuthorities())
              .build();
   }

   private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
      final List<GrantedAuthority> authorities = new ArrayList<>();
      for (final String privilege : privileges) {
         authorities.add(new SimpleGrantedAuthority(privilege));
      }
      return authorities;
   }

   //Teddy's
   private Collection<GrantedAuthority> getGrantedAuthorities (Set<Authority> roles){
      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
   }

}