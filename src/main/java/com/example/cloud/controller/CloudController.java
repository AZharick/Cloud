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
import java.util.stream.Collectors;

import static com.example.cloud.util.Logger.logRed;
import static com.example.cloud.util.Logger.logYellow;
import static com.example.cloud.util.PasswordConcealer.conceal;

@AllArgsConstructor
@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private final AuthenticationService authenticationService;

   @PostMapping("/login")
   public Object login(@RequestBody LoginRequest loginRequest) {
      logYellow("*** LOGIN ATTEMPT ***");
      logYellow("Distinguished from login request: login: " + loginRequest.getLogin() + "; password: " + conceal(loginRequest.getPassword()));
      return authenticationService.login(loginRequest);
   }

   @PostMapping("/logout")
   public void logout(@RequestHeader("auth-token") String authToken) {
      logYellow("*** LOGOUT ATTEMPT ***");
      authenticationService.logout(authToken);
   }

   @PostMapping
   public ResponseEntity<String> uploadFile(
           @RequestHeader("auth-token") String authToken,
           @RequestParam("filename") String filename,
           @RequestParam("file") MultipartFile file) {
      logYellow("*** FILE UPLOAD ATTEMPT ***");

      if (!authenticationService.isTokenValid(authToken)) {
         logRed("Token validation failed during file upload attempt!");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
      }

      User user = userService.getUserByToken(authToken);

      try {
         fileService.save(file, filename, user);
         logYellow("Saving file \"" + filename + "\"...");
         return ResponseEntity.ok("File uploaded successfully");
      } catch (Exception e) {
         logRed("File upload failed");
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
      logYellow("*** GETTING ALL FILES ATTEMPT ***");

      if (!authenticationService.isTokenValid(authToken)) {
         logRed("Token validation failed during listing all files!");
         return new ResponseEntity<>(new Error("Unauthorized", 1), HttpStatus.UNAUTHORIZED);
      }

      if (limit <= 0) {
         logRed("Invalid limit parameter during listing all files!");
         return new ResponseEntity<>(new Error("Limit must be greater than 0", 2), HttpStatus.BAD_REQUEST);
      }

      List<File> userFiles = fileService.getFilesInQtyOf(limit, userService.getUserIdByToken(authToken));
      if (limit > userFiles.size()) {
         limit = userFiles.size();
      }

      List<File> responseFiles = userFiles.stream()
              .limit(limit - 1)
              .map(file -> new File(file.getFilename(), file.getSize()))
              .collect(Collectors.toList());

      return new ResponseEntity<>(responseFiles, HttpStatus.OK);
   }

}