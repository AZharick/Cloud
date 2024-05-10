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
@Table(name="files")
public class File {

   @ManyToOne
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private User user;
   @Id
   private String name;

}