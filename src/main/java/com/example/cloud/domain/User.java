package com.example.cloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column
   private Long id;
   @Column
   private String login;
   @Column
   private String password;

}