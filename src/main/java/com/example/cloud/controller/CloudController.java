package com.example.cloud.controller;

import com.example.cloud.domain.*;
import com.example.cloud.domain.Error;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.cloud.util.CloudLogger.*;
import static com.example.cloud.util.PasswordConcealer.conceal;

@AllArgsConstructor
@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private final AuthenticationService authenticationService;

   @PostMapping("/login")
   public Object login(@RequestBody LoginRequest loginRequest) {
      logInfo("*** LOGIN ATTEMPT ***");
      logInfo("Distinguished from login request: login: " + loginRequest.getLogin() + "; password: " + conceal(loginRequest.getPassword()));
      return authenticationService.login(loginRequest);
   }

   @PostMapping("/logout")
   public void logout(@RequestHeader("auth-token") String authToken) {
      logInfo("*** LOGOUT ATTEMPT ***");
      authenticationService.logout(authToken);
   }

   @PostMapping
   public ResponseEntity<String> uploadFile(
           @RequestHeader("auth-token") String authToken,
           @RequestParam("filename") String filename,
           @RequestParam("file") MultipartFile file) {
      logInfo("*** FILE UPLOAD ATTEMPT ***");

      if (!authenticationService.isTokenValid(authToken)) {
         logSevere("Token validation failed during file upload attempt!");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
      }

      User user = userService.getUserByToken(authToken);

      try {
         fileService.save(file, filename, user);
         logInfo("Saving file \"" + filename + "\"...");
         return ResponseEntity.ok("File uploaded successfully");
      } catch (Exception e) {
         logSevere("File upload failed");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
      }
   }

   @DeleteMapping("/file")
   public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
      logInfo("*** DELETE FILE ATTEMPT ***");

      if (authenticationService.isTokenValid(authToken)) {
         logInfo("Token OK");

         Optional<File> fileFromDB = fileService.findByFilename(filename);
         if (fileFromDB.isPresent()) {
            fileService.delete(fileFromDB.get());
            logInfo("File deleted successfully!");
            return ResponseEntity.ok("File deleted successfully!");
         } else {
            logSevere("File not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found!");
         }

      } else {
         logSevere("Token validation failed during file delete attempt!");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed during file delete attempt!");
      }
   }

   @GetMapping("/file")
   public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
      logInfo("*** DOWNLOAD ATTEMPT ***");

      if (!authenticationService.isTokenValid(authToken)) {
         logSevere("Token validation failed during file download attempt!");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body("Token validation failed during file download attempt".getBytes());
      }

      logInfo("Token OK!");
      Optional<File> fileFromDB = fileService.findByFilename(filename);

      if (fileFromDB.isPresent()) {
         File file = fileFromDB.get();
         return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                 .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()))
                 .body(file.getFile());
      } else {
         logSevere("File not found!");
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
   }

   @PutMapping
   public ResponseEntity<?> renameFile(
           @RequestHeader("auth-token") String authToken,
           @RequestParam String newFilename,
           @RequestBody RenameFileRequest request) throws FileNotFoundException {
      logInfo("*** RENAME ATTEMPT ***");

      if (authenticationService.isTokenValid(authToken)) {
         Optional<File> renamedFile = fileService.renameFile(newFilename, request.getCurrentFilename());
         return ResponseEntity.ok(renamedFile);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body("Invalid auth token");
      }

   }

   @GetMapping("/list")
   public ResponseEntity<?> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
      logInfo("*** GETTING ALL FILES ATTEMPT ***");

      if (!authenticationService.isTokenValid(authToken)) {
         logSevere("Token validation failed during listing all files!");
         return new ResponseEntity<>(new Error("Unauthorized", 1), HttpStatus.UNAUTHORIZED);
      }

      if (limit <= 0) {
         logSevere("Invalid limit parameter during listing all files!");
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