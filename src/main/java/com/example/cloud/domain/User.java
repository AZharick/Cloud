package com.example.cloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@SequenceGenerator(name = "customGen", allocationSize = 1)
public class User implements UserDetails, Serializable {

   @Id
   @Column(nullable = false, unique = true)
   private String username;

   @Column(nullable = false)
   private String password;

   @OneToMany(cascade = CascadeType.ALL)
   private List<File> userFiles;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
   }

   @Override
   public String getPassword() {
      return this.password;
   }

   @Override
   public String getUsername() {
      return this.username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}