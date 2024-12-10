package com.example.cloud.service;

import com.example.cloud.repository.UserRepository;
import com.example.cloud.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {
   @Mock
   private UserRepository userRepository;

   @InjectMocks
   private UserService userService;

   private User mockUser;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.openMocks(this);
      mockUser = new User();
      mockUser.setId(1L);
      mockUser.setUsername("Test User");
   }

   @Test
   public void testGetUserByToken_UserExists() {
      String token = "validToken";
      when(userRepository.getUserByToken(token)).thenReturn(mockUser);

      User result = userService.getUserByToken(token);

      assertNotNull(result);
      assertEquals(mockUser.getId(), result.getId());
      assertEquals(mockUser.getUsername(), result.getUsername());
      verify(userRepository, times(1)).getUserByToken(token);
   }

   @Test
   public void testGetUserByToken_UserDoesNotExist() {
      String token = "invalidToken";
      when(userRepository.getUserByToken(token)).thenReturn(null);

      User result = userService.getUserByToken(token);

      assertNull(result);
      verify(userRepository, times(1)).getUserByToken(token);
   }

   @Test
   public void testGetPasswordByUsername_UserExists() {
      String username = "testUser";
      String expectedPassword = "securePassword";

      when(userRepository.getPasswordByUsername(username)).thenReturn(expectedPassword);

      String result = userService.getPasswordByUsername(username);

      assertNotNull(result);
      assertEquals(expectedPassword, result);
      verify(userRepository, times(1)).getPasswordByUsername(username);
   }

   @Test
   public void testGetPasswordByUsername_UserDoesNotExist() {
      String username = "nonExistentUser";

      when(userRepository.getPasswordByUsername(username)).thenReturn(null);

      Exception exception = assertThrows(RuntimeException.class, () -> userService.getPasswordByUsername(username));

      assertEquals("User not found or password is null", exception.getMessage());
      verify(userRepository, times(1)).getPasswordByUsername(username);
   }

   @Test
   public void testGetTokenByUsername_UserExists() {
      String username = "testUser";
      String expectedToken = "secureToken";

      when(userRepository.getTokenByUsername(username)).thenReturn(expectedToken);

      String result = userService.getTokenByUsername(username);

      assertNotNull(result);
      assertEquals(expectedToken, result);
      verify(userRepository, times(1)).getTokenByUsername(username);
   }

   @Test
   public void testGetTokenByUsername_UserDoesNotExist() {
      String username = "nonExistentUser";

      when(userRepository.getTokenByUsername(username)).thenReturn(null);

      Exception exception = assertThrows(RuntimeException.class, () -> {
         userService.getTokenByUsername(username);
      });

      assertEquals("User not found or token is null", exception.getMessage());
      verify(userRepository, times(1)).getTokenByUsername(username);
   }

   @Test
   public void testUpdateTokenByUsername() {
      String authToken = "newAuthToken";
      String username = "testUser";

      userService.updateTokenByUsername(authToken, username);

      verify(userRepository, times(1)).updateTokenByUsername(authToken, username);
   }

   @Test
   public void testDeleteTokenByUsername() {
      String username = "testUser";

      userService.deleteTokenByUsername(username);

      verify(userRepository, times(1)).deleteTokenByUsername(username);
   }

   @Test
   public void testGetUserIdByToken() {
      String token = "validToken";
      int expectedUserId = 123;

      when(userRepository.getUserIdByToken(token)).thenReturn(expectedUserId);

      int result = userService.getUserIdByToken(token);

      assertEquals(expectedUserId, result);
      verify(userRepository, times(1)).getUserIdByToken(token);
   }

}