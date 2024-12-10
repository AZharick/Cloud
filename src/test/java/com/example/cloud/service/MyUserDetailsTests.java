package com.example.cloud.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyUserDetailsTests {

   private User user;
   private MyUserDetails myUserDetails;

   @BeforeEach
   public void setUp() {
      user = Mockito.mock(User.class);
      myUserDetails = new MyUserDetails(user);
   }

   @Test
   public void testGetAuthorities() {
      Set<User> users = new HashSet<>();
      users.add(user);
      Authority fullAuth = new Authority(1, "full", users);
      Set<Authority> authorities = new HashSet<>();
      authorities.add(fullAuth);
      when(user.getAuthorities()).thenReturn(authorities);

      Collection<? extends GrantedAuthority> userAuthorities = myUserDetails.getAuthorities();
      assertEquals(1, authorities.size());
      assertTrue(userAuthorities.contains(new SimpleGrantedAuthority("full")));
   }

   @Test
   public void testGetPassword() {
      String password = "testPassword";
      when(user.getPassword()).thenReturn(password);
      assertEquals(password, myUserDetails.getPassword());
   }

   @Test
   public void testGetUsername() {
      String username = "testUser";
      when(user.getUsername()).thenReturn(username);
      assertEquals(username, myUserDetails.getUsername());
   }

   @Test
   public void testIsAccountNonExpired() {
      assertTrue(myUserDetails.isAccountNonExpired());
   }

   @Test
   public void testIsAccountNonLocked() {
      assertTrue(myUserDetails.isAccountNonLocked());
   }

   @Test
   public void testIsCredentialsNonExpired() {
      assertTrue(myUserDetails.isCredentialsNonExpired());
   }

   @Test
   public void testIsEnabled() {
      assertTrue(myUserDetails.isEnabled());
   }

}