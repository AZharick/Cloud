package com.example.cloud.controller;

import com.example.cloud.domain.*;
import com.example.cloud.domain.Error;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@AllArgsConstructor
@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private final AuthenticationService authenticationService;

   @PostMapping("/login")
   public Object login(@RequestBody LoginRequest loginRequest) {
      return authenticationService.login(loginRequest);
   }

   @PostMapping("/logout")
   public void logout(@RequestHeader("auth-token") String authToken) {
      authenticationService.logout(authToken);
   }

   @PostMapping
   public ResponseEntity<String> uploadFile(
           @RequestHeader("auth-token") String authToken,
           @RequestParam("filename") String filename,
           @RequestParam("file") MultipartFile file) {

      if (!authenticationService.isTokenValid(authToken)) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
      }

      User user = userService.getUserByToken(authToken);

      try {
         fileService.save(file, filename, user);
         return ResponseEntity.ok("File uploaded successfully");
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
      }
   }

   @DeleteMapping("/file")
   public void deleteFile() {
      //in: header String "auth-token"
      //in query - String filename
   }

   @GetMapping("/file")
   public void downloadFile() {
      //in: header String "auth-token"
      //in query - String filename
   }

   @PutMapping("/file")
   public void renameFile() {
      //in: header String "auth-token"
      //in query - String filename
      //requestBody: Login and password hash
   }

   @GetMapping("/list")
   public ResponseEntity<?> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {

      if (!authenticationService.isTokenValid(authToken)) {
         return new ResponseEntity<>(new Error("Unauthorized", 1), HttpStatus.UNAUTHORIZED);
      }

      if (limit <= 0) {
         return new ResponseEntity<>(new Error("Limit must be greater than 0", 2), HttpStatus.BAD_REQUEST);
      }

      List<File> userFiles = fileService.getFilesInQtyOf(limit, userService.getUserIdByToken(authToken));

      if (limit > userFiles.size()) {
         limit = userFiles.size();
      }

      List<File> responseFiles = new ArrayList<>();
      for (int i = 0; i < limit - 1; i++) {
         File file = userFiles.get(i);
         responseFiles.add(new File(file.getFilename(), file.getSize()));
      }

      return new ResponseEntity<>(responseFiles, HttpStatus.OK);
   }

}