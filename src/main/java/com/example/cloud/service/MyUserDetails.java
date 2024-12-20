package com.example.cloud.service;

import com.example.cloud.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MyUserDetails implements UserDetails {

   private User user;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return user.getAuthorities().stream()
              .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
              .collect(Collectors.toList());
   }

   @Override
   public String getPassword() { return user.getPassword(); }
   @Override
   public String getUsername() { return user.getUsername(); }
   @Override
   public boolean isAccountNonExpired() { return true; }
   @Override
   public boolean isAccountNonLocked() { return true; }
   @Override
   public boolean isCredentialsNonExpired() {return true; }
   @Override
   public boolean isEnabled() { return true; }

}