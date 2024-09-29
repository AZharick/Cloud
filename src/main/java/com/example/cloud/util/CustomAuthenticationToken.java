package com.example.cloud.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//n whole class

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

   private final Set<GrantedAuthority> authorities;

   public CustomAuthenticationToken(Object principal, Object credentials, Collection<GrantedAuthority> authorities) {
      super(principal, credentials, authorities);
      this.authorities = new HashSet<>(authorities);
   }

   @Override
   public Collection<GrantedAuthority> getAuthorities() {
      return this.authorities;
   }

}