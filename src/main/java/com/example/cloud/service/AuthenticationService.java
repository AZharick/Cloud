package com.example.cloud.service;

import com.example.cloud.model.User;
import com.example.cloud.repository.UserTokenRepository;
import com.example.cloud.util.TokenGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.cloud.util.CloudLogger.*;

@Service
@AllArgsConstructor
public class AuthenticationService implements AuthenticationManager {
   private UserTokenRepository userTokenRepository;
   private UserService userService;

   public String getToken(String username, String password) {
      logInfo("*** SERVICE: getToken ***");
      User currentUser = User.builder().username(username).password(password).build();
      Authentication tokenToAuthenticate = this.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      if (!tokenToAuthenticate.isAuthenticated()) {
         logSevere("Token authentication failed!");
         throw new AuthenticationException("Bad credentials") {
         };
      }

      SecurityContextHolder.getContext().setAuthentication(tokenToAuthenticate);
      logInfo("Token authenticated successfully!");
      String authToken = TokenGenerator.generateUUIDToken();
      userService.mapTokenToUser(authToken, currentUser);
      logInfo("Token generated: " + authToken);
      logInfo("Token mapped to user " + username);
      logInfo("exiting SERVICE: getToken...");
      logInfo("userTokens: " + userTokenRepository.printTokensAndUsers());
      return authToken;
   }

   @Override
   public Authentication authenticate(Authentication tokenToAuthenticate) throws AuthenticationException {
      logInfo("*** SERVICE.authenticate ***");

      String usernameToAuthenticate = tokenToAuthenticate.getPrincipal().toString();
      UserDetails userDetails = userService.loadUserByUsername(usernameToAuthenticate);

      if (userDetails == null) {
         logSevere("UserDetails not found!");
         throw new BadCredentialsException("Bad credentials");
      }

      String passwordToAuthenticate = tokenToAuthenticate.getCredentials().toString();
      String passwordFromDB = userService.getPasswordByUsername(usernameToAuthenticate);

      if (!passwordToAuthenticate.equals(passwordFromDB)) {
         logSevere("Password invalid!");
         throw new BadCredentialsException("Bad credentials during authenticate() method!");
      }

      logInfo("Password valid!");
      GrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
      List<GrantedAuthority> roles = new ArrayList<>();
      roles.add(adminRole);
      UsernamePasswordAuthenticationToken tokenResponse = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), passwordToAuthenticate, roles);

      //checkpoint
      logInfo("Token: " + tokenResponse);
      logInfo("exiting SERVICE.authenticate...");
      return tokenResponse;
   }

   public void logout(String authToken) {
      logInfo("*** SERVICE: LOGOUT w/token " + authToken + " ***");
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && userTokenRepository.isTokenPresent(authToken)) {
         userTokenRepository.deleteUserByToken(authToken);
      }
      SecurityContextHolder.getContext().setAuthentication(null);
   }

}