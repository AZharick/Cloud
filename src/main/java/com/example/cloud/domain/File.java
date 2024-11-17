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

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private User user;

   @Column(name = "filename", nullable = false)
   private String filename;

   @Column(nullable = false, unique = true)
   private String hash; // id req by API

   @Lob
   private byte[] file;

   @Column(name = "size", nullable = false)
   private int size;

   public File(String filename, int size) {
      this.filename = filename;
      this.size = size;
   }
}