package com.example.cloud.service;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.User;
import com.example.cloud.repository.UserRepository;
import com.example.cloud.util.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.example.cloud.util.Logger.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTests {

   @Mock
   private UserRepository userRepository;

   @InjectMocks
   private CustomUserDetailsService customUserDetailsService;

   private User mockUser;

   @BeforeEach
   public void setUp() throws IOException {
      MockitoAnnotations.openMocks(this);
      mockUser = new User();
      mockUser.setUsername("testUser");
      mockUser.setPassword("testPassword");

      Set<User> userSet = new HashSet<>();         // CAUTION HERE
      userSet.add(mockUser);
      Set<Authority> authoritySet = new HashSet<>();
      authoritySet.add(new Authority(1, "full", userSet));
      mockUser.setAuthorities(authoritySet);

      initLog();
   }

   @Test
   public void testLoadUserByUsername_UserExists() {
      String username = "testUser";

      when(userRepository.existsUserByUsername(username)).thenReturn(true);
      when(userRepository.getUserByUsername(username)).thenReturn(mockUser);

      UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

      assertNotNull(userDetails);
      assertEquals("testUser", userDetails.getUsername());
      assertEquals("testPassword", userDetails.getPassword());
      verify(userRepository).existsUserByUsername(username);
      verify(userRepository).getUserByUsername(username);
   }

   @Test
   public void testLoadUserByUsername_UserNotFound() {
      String username = "nonExistentUser";

      when(userRepository.existsUserByUsername(username)).thenReturn(false);

      assertThrows(UsernameNotFoundException.class, () -> {
         customUserDetailsService.loadUserByUsername(username);
      });

      verify(userRepository).existsUserByUsername(username);
      verify(userRepository, never()).getUserByUsername(anyString());
   }
}
