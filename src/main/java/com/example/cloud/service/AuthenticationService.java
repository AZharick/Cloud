package com.example.cloud.service;

import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.util.CustomAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthenticationService implements AuthenticationManager {

   private AuthorityRepository authorityRepository;

   public AuthenticationService(AuthorityRepository authorityRepository) {
      this.authorityRepository = authorityRepository;
   }

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      System.out.println("\nAUTHENTICATION ATTEMPT:");
      System.out.println("getName: " + authentication.getName());             // -
      System.out.println("principal: " + authentication.getPrincipal());      // null
      System.out.println("credentials: " + authentication.getCredentials());  // asd
      System.out.println("authority: " + authentication.getAuthorities());    // []
      System.out.println("details: " + authentication.getDetails());          // null

      Collection<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(authorityRepository.findById(1));
      System.out.println("adding authorities: " + authorities);

      //check condition
      if(authentication.getName().equals(authentication.getCredentials())){
         return new CustomAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
      }
      throw new BadCredentialsException("Very Bad credentials");
   }

}