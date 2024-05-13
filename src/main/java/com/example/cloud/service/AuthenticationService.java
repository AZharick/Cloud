package com.example.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service //n whole class
public class AuthenticationService {

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

}