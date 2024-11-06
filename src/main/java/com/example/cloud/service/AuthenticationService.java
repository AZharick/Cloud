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

import static com.example.cloud.util.ColorTxt.writeInYellow;
import static com.example.cloud.util.ColorTxt.writeInGreen;
import static com.example.cloud.util.ColorTxt.writeInRed;

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
      writeInYellow("\n> LOGIN ATTEMPT:");
      this.loginRequest = loginRequest;
      String username = loginRequest.getLogin();       // asd
      String password = loginRequest.getPassword();    // asd
      writeInYellow("> Distinguished from loginRequest: username= " + username +
              ", password: " + password);

      try {
         authenticatedToken = this.authenticate(new UsernamePasswordAuthenticationToken(username, password));

         if (authenticatedToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            writeInGreen("> LOGIN SUCCESS!");
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

   @Override //todo
   public Authentication authenticate(Authentication tokenToAuthenticate) throws AuthenticationException {
      writeInYellow("\n> AUTHENTICATION ATTEMPT:");

      Collection<GrantedAuthority> authoritiesForToken = new ArrayList<>();
      authoritiesForToken.add(authorityRepository.findById(1));
      writeInYellow("\n> Authorities set for authedToken: " + authoritiesForToken);

      //checkpoint
      String tokenUsername = tokenToAuthenticate.getPrincipal().toString();
      String tokenPassword = tokenToAuthenticate.getCredentials().toString();
      String passwordFromDB = userService.getPasswordByUsername(tokenUsername);

      if(tokenPassword.equals(passwordFromDB)){
         writeInGreen("> Password valid!");
         CustomAuthenticationToken authedToken = new CustomAuthenticationToken(tokenUsername, tokenPassword, authoritiesForToken);
         writeInYellow("token authed: " + authedToken.isAuthenticated());
         //todo UDS from N
         return authedToken;
      }
      throw new BadCredentialsException("Bad credentials during authenticate()");
   }

   public void logout (String authToken) {
      if (authToken.equals(this.authenticatedToken.getCredentials())) { //todo not credentials
         userService.deleteTokenByUsername(loginRequest.getLogin());
      }
      SecurityContextHolder.getContext().setAuthentication(null);
   }

   public boolean isTokenValid(String authToken) {
      return authenticatedToken.getCredentials().equals(authToken); // todo getCredentials wrong?
   }

}