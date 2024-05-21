package com.example.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service //n whole class
public class AuthenticationService implements AuthenticationManager {

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      if(authentication.getName().equals(authentication.getCredentials())){
         return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
      }
      throw new BadCredentialsException("Bad credentials");
   }

//   private final AuthenticationManager authenticationManager;
//
//   @Autowired
//   public AuthenticationService(AuthenticationManager authenticationManager) {
//      this.authenticationManager = authenticationManager;
//   }
//
//   public void authenticateUser(String username, String password) {
//      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
//      Authentication authentication = authenticationManager.authenticate(authToken);
//
//      SecurityContextHolder.getContext().setAuthentication(authentication);
//   }

   //neto notes


}