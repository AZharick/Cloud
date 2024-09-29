package com.example.cloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
   private String authority;

   //@ManyToMany(mappedBy = "authorities")
   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name="user_authority",
           joinColumns=  @JoinColumn(name="authority_id", referencedColumnName="id"),
           inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="id") )
   private Set<User> users = new HashSet<>();
}