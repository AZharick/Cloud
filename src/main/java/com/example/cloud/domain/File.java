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

   @Column(name = "filename", nullable = false) //n
   private String filename;

   @Column(name = "size", nullable = false) //n
   private Long size;

   @Lob
   private byte[] fileData;


}