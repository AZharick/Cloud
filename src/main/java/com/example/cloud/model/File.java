package com.example.cloud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class File {

   @Id
   @Column(nullable = false, unique = true)
   private String filename;

   @Column(nullable = false)
   private LocalDateTime date;

   @Column(nullable = false)
   private Long size;

   @Lob
   @Column(nullable = false)
   private byte[] fileData;

   @ManyToOne
   private User user;

   private String username;

   public File(String filename, Long size) {
      this.filename = filename;
      this.size = size;
   }

}