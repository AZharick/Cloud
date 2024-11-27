package com.example.cloud.service;

import com.example.cloud.domain.Error;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.LoginRequest;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.util.TokenGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

import static com.example.cloud.util.Logger.logYellow;
import static com.example.cloud.util.Logger.logGreen;
import static com.example.cloud.util.Logger.logRed;

@Service
@AllArgsConstructor
public class AuthenticationService implements AuthenticationManager {
   private UserService userService;
   private AuthorityRepository authorityRepository;
   private CustomUserDetailsService customUserDetailsService;

   //my
   public Object login(LoginRequest loginRequest) {
      String username = loginRequest.getLogin();       // asd
      String password = loginRequest.getPassword();    // asd

      Authentication authenticatedToken;
      try {
         logYellow("*** AUTHENTICATION ATTEMPT ***");
         authenticatedToken = this.authenticate(new UsernamePasswordAuthenticationToken(username, password));
         if (authenticatedToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            logGreen("Token authenticated successfully!");
         } else {
            logRed("Token authentication failed!");
            throw new Error("Bad credentials", 400);
         }
      } catch (Error e) {
         return e;
      }

      String authToken = TokenGenerator.generateUUIDToken();
      if(!authToken.isEmpty()) {
         logGreen("Token generated successfully!");
      }
      userService.updateTokenByUsername(authToken, username);

      Login login = new Login();
      login.setAuthToken(authToken);
      return ResponseEntity.ok(login);
   }

   @Override
   public Authentication authenticate(Authentication tokenToAuthenticate) throws AuthenticationException {
      Collection<GrantedAuthority> authoritiesForToken = new ArrayList<>();
      GrantedAuthority authorityFromDB = authorityRepository.findById(1);
      authoritiesForToken.add(authorityFromDB);

      String usernameToAuthenticate = tokenToAuthenticate.getPrincipal().toString();
      String passwordToAuthenticate = tokenToAuthenticate.getCredentials().toString();
      String passwordFromDB = userService.getPasswordByUsername(usernameToAuthenticate);
      logYellow("Authority \"" + authorityFromDB + "\" assigned to user " + usernameToAuthenticate);

      UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameToAuthenticate);

      if (userDetails == null) {
         logRed("UserDetails not found!");
         throw new BadCredentialsException("Invalid username!");
      }

      if (passwordToAuthenticate.equals(passwordFromDB)) {
         logGreen("Password valid!");
         return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), passwordToAuthenticate, userDetails.getAuthorities());
      } else {
         logRed("Password invalid!");
         throw new BadCredentialsException("Bad credentials during authenticate() method!");
      }
   }

   public void logout(String authToken) {
      if (isTokenValid(authToken)) {
         userService.deleteTokenByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
      }
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   public boolean isTokenValid(String authToken) {
      String userTokenFromDB = userService.getTokenByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
      return userTokenFromDB.equals(authToken);
   }

}