package com.example.cloud.service;

import com.example.cloud.domain.Error;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.LoginRequest;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.util.CustomAuthenticationToken;
import com.example.cloud.util.TokenGenerator;
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

@Service
public class AuthenticationService implements AuthenticationManager {
   private UserService userService;
   private AuthorityRepository authorityRepository;
   private LoginRequest loginRequest;
   private Authentication authenticatedToken;

   public AuthenticationService(UserService userService, AuthorityRepository authorityRepository) {
      this.userService = userService;
      this.authorityRepository = authorityRepository;
   }

   public Object login (LoginRequest loginRequest) {
      System.out.println("\nLOGIN ATTEMPT:");
      this.loginRequest = loginRequest;
      String username = loginRequest.getLogin();
      String password = loginRequest.getPassword();
      //System.out.printf("Username: %s, password: %s\n", username, password);

      try {
         Authentication authToken = new UsernamePasswordAuthenticationToken(username, password);
         authenticatedToken = this.authenticate(authToken);
         if (authenticatedToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            System.out.println("LOGIN SUCCESS!");
         } else throw new Error("Bad credentials", 400);
      } catch (Error e) {
         return e;
      }

      String authToken = TokenGenerator.generateUUIDToken();
      userService.updateTokenByUsername(authToken, username);
      Login login = new Login();
      login.setAuthToken(authToken);
      return login;
   }

   @Override
   public Authentication authenticate(Authentication authToken) throws AuthenticationException {
      System.out.println("\nAUTHENTICATION ATTEMPT:");

      Collection<GrantedAuthority> authoritiesForToken = new ArrayList<>();
      authoritiesForToken.add(authorityRepository.findById(1));

      System.out.printf("\nname: %s, credentials: %s, authorities: %s",
              authToken.getName(), authToken.getCredentials(), authToken.getAuthorities());

      //check condition
      if(authToken.getName().equals(authToken.getCredentials())){
         return new CustomAuthenticationToken(authToken.getPrincipal(), authToken.getCredentials(), authoritiesForToken);
      }
      throw new BadCredentialsException("Bad credentials while: authenticate()");
   }

   public void logout (String authToken) {
      if (authToken.equals(this.authenticatedToken.getCredentials())) {
         userService.deleteTokenByUsername(loginRequest.getLogin());
      }
      SecurityContextHolder.getContext().setAuthentication(null);
   }

}