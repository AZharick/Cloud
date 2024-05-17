package com.example.cloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@SequenceGenerator(name = "customGen", allocationSize = 1)
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "customGen")
   private Long id;
   private String password;
   private String username;

   @ManyToMany
   @JoinTable(
           name = "user_authority",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "authority_id")
   )
   private Set<Authority> authorities = new HashSet<>();

}