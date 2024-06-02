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
   //working -> changed
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      if(!userRepository.existsUserByUsername(username)) {
         throw new UsernameNotFoundException("User " + username + " not found");
      }

      User user = userRepository.getUserByUsername(username);

      //n
      return (UserDetails) User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .authorities(user.getAuthorities())
              .build();
   }

   //https://github.com/Baeldung/spring-security-registration/blob/master/src/main/java/com/baeldung/security/MyUserDetailsService.java
//   @Override
//   public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
//      try {
//         final User user = userRepository.getUserByUsername(username);
//         if (user == null) {
//            throw new UsernameNotFoundException("No user found with username: " + username);
//         }
//
//         return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
//      } catch (final Exception e) {
//         throw new RuntimeException(e);
//      }
//   }

   // Baeldung's UTIL
//   private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
//      return getGrantedAuthorities(getPrivileges(roles));
//   }
//
//   private List<String> getPrivileges(final Collection<Role> roles) {
//      final List<String> privileges = new ArrayList<>();
//      final List<Privilege> collection = new ArrayList<>();
//      for (final Role role : roles) {
//         privileges.add(role.getName());
//         collection.addAll(role.getPrivileges());
//      }
//      for (final Privilege item : collection) {
//         privileges.add(item.getName());
//      }
//
//      return privileges;
//   }

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
