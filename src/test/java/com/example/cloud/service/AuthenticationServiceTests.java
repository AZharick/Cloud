package com.example.cloud.service;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.LoginRequest;
import com.example.cloud.repository.AuthorityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static com.example.cloud.util.Logger.*;

public class AuthenticationServiceTests {

   @Mock
   private UserService userService;

   @Mock
   private AuthorityRepository authorityRepository;

   @Mock
   private CustomUserDetailsService customUserDetailsService;

   @InjectMocks
   private AuthenticationService authenticationService;

   @BeforeEach
   public void setUp() throws IOException {
      MockitoAnnotations.openMocks(this);
      initLog();
   }

   @Test
   public void testLogin_Success() {
      LoginRequest loginRequest = new LoginRequest("asd", "asd");
      when(userService.getPasswordByUsername("asd")).thenReturn("asd");
      when(customUserDetailsService.loadUserByUsername("asd")).thenReturn(mock(UserDetails.class));

      Authority authority = new Authority();
      authority.setId(1);
      authority.setAuthority("full");

      when(authorityRepository.findById(1)).thenReturn(authority);

      ResponseEntity<Login> response = (ResponseEntity<Login>) authenticationService.login(loginRequest);

      assertEquals(HttpStatus.OK, response.getStatusCode());
      Assertions.assertNotNull(response.getBody());
      Login loginResponse = (Login) response.getBody();
      Assertions.assertNotNull(loginResponse.getAuthToken());
      verify(userService).updateTokenByUsername(anyString(), eq("asd"));
   }


   @Test
   public void testLogin_BadCredentials() {
      LoginRequest loginRequest = new LoginRequest("asd", "wrongPassword");
      when(userService.getPasswordByUsername("asd")).thenReturn("asd");

      Exception exception = assertThrows(Exception.class, () -> authenticationService.login(loginRequest));
      assertEquals("Bad credentials", exception.getMessage());
   }

   @Test
   public void testAuthenticate_Success() {
      String username = "user";
      String password = "password";
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

      when(userService.getPasswordByUsername(username)).thenReturn(password);
      when(customUserDetailsService.loadUserByUsername(username)).thenReturn(mock(UserDetails.class));

      Authentication result = authenticationService.authenticate(token);

      assertNotNull(result);
      assertTrue(result.isAuthenticated());
   }

   @Test
   public void testAuthenticate_InvalidUser() {
      String username = "invalidUser";
      String password = "password";
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

      when(userService.getPasswordByUsername(username)).thenReturn(password);
      when(customUserDetailsService.loadUserByUsername(username)).thenReturn(null);

      assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(token));
   }

   @Test
   public void testLogout_ValidToken() {
      String authToken = "validToken";

      Authentication authentication = Mockito.mock(Authentication.class);
      when(authentication.getName()).thenReturn("testName");

      SecurityContextHolder.getContext().setAuthentication(authentication);

      when(userService.getTokenByUsername(anyString())).thenReturn(authToken);

      authenticationService.logout(authToken);

      verify(userService).deleteTokenByUsername("testName");
   }


   @Test
   public void testIsTokenValid_ValidToken() {
      // Arrange
      String authToken = "validToken";
      when(userService.getTokenByUsername(anyString())).thenReturn(authToken);

      // Act
      boolean isValid = authenticationService.isTokenValid(authToken);

      // Assert
      assertTrue(isValid);
   }

   @Test
   public void testIsTokenValid_InvalidToken() {
      // Arrange
      String authToken = "validToken";
      when(userService.getTokenByUsername(anyString())).thenReturn("invalidToken");

      // Act
      boolean isValid = authenticationService.isTokenValid(authToken);

      // Assert
      assertFalse(isValid);
   }

}