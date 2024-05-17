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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
   private String authority;

   @ManyToMany(mappedBy = "authorities")
   private Set<User> users = new HashSet<>();
}