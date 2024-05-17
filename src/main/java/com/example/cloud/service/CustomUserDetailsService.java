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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

   private UserRepository userRepository;

   @Autowired
   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   //извлечение Usera из БД
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.getUserByUsername(username).orElseThrow(
              () -> new UsernameNotFoundException("User " + username + " not found"));

      //return new User(user.getUsername(), user.getPassword(), user.getRoles())        //Teddy's
      //return new User(user.getLogin(), user.getPassword(), user.getUsername());      //my try

      //n
      return (UserDetails) User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .authorities(user.getAuthorities())
              .build();
   }

   //Teddy's
//   private Collection<GrantedAuthority> map (List<Authority> roles){
//      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
//   }

}
