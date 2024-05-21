package com.example.cloud.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service //neto whole class - see page 105
public class AuthenticationService implements AuthenticationManager {

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      if(authentication.getName().equals(authentication.getCredentials())){
         return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
      }
      throw new BadCredentialsException("Bad credentials");
   }

}