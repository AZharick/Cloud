package com.example.cloud.service;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.Error;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.LoginRequest;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.util.TokenGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.example.cloud.util.CloudLogger.*;

@Service
@AllArgsConstructor
public class AuthenticationService implements AuthenticationManager {

   private UserService userService;
   private AuthorityRepository authorityRepository;
   private CustomUserDetailsService customUserDetailsService;

   public Object login(LoginRequest loginRequest) {
      String username = loginRequest.getLogin();       // asd
      String password = loginRequest.getPassword();    // asd

      Authentication authenticatedToken;
      try {
         logInfo("*** AUTHENTICATION ATTEMPT ***");
         authenticatedToken = this.authenticate(new UsernamePasswordAuthenticationToken(username, password));
         if (authenticatedToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            logInfo("Token authenticated successfully!");
         } else {
            logSevere("Token authentication failed!");
            throw new Error("Bad credentials", 400);
         }
      } catch (Error e) {
         return e;
      }

      String authToken = TokenGenerator.generateUUIDToken();
      if(!authToken.isEmpty()) {
         logInfo("Token generated successfully!");
      }
      userService.updateTokenByUsername(authToken, username);

      Login login = new Login();
      login.setAuthToken(authToken);
      return ResponseEntity.ok(login);
   }

   @Override
   public Authentication authenticate(Authentication tokenToAuthenticate) throws AuthenticationException {
      Collection<Optional<Authority>> authoritiesForToken = new ArrayList<>();
      Optional<Authority> authorityFromDB = authorityRepository.findById(1);
      authoritiesForToken.add(authorityFromDB);

      String usernameToAuthenticate = tokenToAuthenticate.getPrincipal().toString();
      String passwordToAuthenticate = tokenToAuthenticate.getCredentials().toString();
      String passwordFromDB = userService.getPasswordByUsername(usernameToAuthenticate);
      logInfo("Authority \"" + authorityFromDB + "\" assigned to user " + usernameToAuthenticate);

      UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameToAuthenticate);

      if (userDetails == null) {
         logSevere("UserDetails not found!");
         throw new BadCredentialsException("Bad credentials");
      }

      if (passwordToAuthenticate.equals(passwordFromDB)) {
         logInfo("Password valid!");
         return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), passwordToAuthenticate, userDetails.getAuthorities());
      } else {
         logSevere("Password invalid!");
         throw new BadCredentialsException("Bad credentials during authenticate() method!");
      }
   }

   public void logout(String authToken) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && isTokenValid(authToken)) {
         userService.deleteTokenByUsername(authentication.getName());
      }
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   public boolean isTokenValid(String authToken) {
      String userTokenFromDB = userService.getTokenByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
      return userTokenFromDB.equals(authToken);
   }

}