package com.example.cloud.controller;

import com.example.cloud.dto.LoginResponse;
import com.example.cloud.model.*;
import com.example.cloud.dto.FileResponse;
import com.example.cloud.dto.LoginRequest;
import com.example.cloud.dto.RenameFileRequest;
import com.example.cloud.repository.UserTokenRepository;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.*;

import static com.example.cloud.util.CloudLogger.*;
import static com.example.cloud.util.PasswordConcealer.conceal;

@AllArgsConstructor
@RestController
public class CloudController {
   private final UserTokenRepository userTokenRepository;
   private final FileService fileService;
   private final AuthenticationService authenticationService;

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
      logInfo("*** CONTROLLER: LOGIN ***");
      String username = loginRequest.getLogin();       // asd
      String password = loginRequest.getPassword();    // asd
      logInfo("Parsing request: USERNAME: " + username + "; PASSWORD: " + conceal(password));

      LoginResponse loginResponse = new LoginResponse(authenticationService.getToken(username, password));
      logInfo(loginResponse.toString());

      return new ResponseEntity<>(loginResponse, HttpStatus.OK);
   }

   @PostMapping("/logout")
   public void logout(@RequestHeader("auth-token") String authToken) {
      logInfo("*** CONTROLLER: LOGOUT ***");
      authenticationService.logout(authToken);
   }

   @GetMapping("/list")
   public List<FileResponse> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit) {
      logInfo("*** CONTROLLER: LIST FILES ***");
      return fileService.getFiles(authToken, limit);
   }

   @PostMapping
   public ResponseEntity<String> uploadFile(
           @RequestHeader("auth-token") String authToken,
           @RequestParam("filename") String filename,
           @RequestParam("file") MultipartFile file) {
      logInfo("*** CONTROLLER: FILE UPLOAD ***");

      if (!userTokenRepository.isTokenPresent(authToken)) {
         logSevere("Token validation failed during file upload attempt!");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
      }

      try {
         fileService.save(file, filename, userTokenRepository.getUserByToken(authToken));
         logInfo("Saving file \"" + filename + "\"...");
         return ResponseEntity.ok("File uploaded successfully");
      } catch (Exception e) {
         logSevere("File upload failed");
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
      }
   }

   @DeleteMapping("/file")
   public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
      logInfo("*** CONTROLLER: DELETE FILE ***");

      if (userTokenRepository.isTokenPresent(authToken)) {
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
      logInfo("*** CONTROLLER: DOWNLOAD ***");

      if (!userTokenRepository.isTokenPresent(authToken)) {
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
                 .body(file.getFileData());
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
      logInfo("*** CONTROLLER: RENAME FILE ***");

      if (userTokenRepository.isTokenPresent(authToken)) {
         Optional<File> renamedFile = fileService.renameFile(newFilename, request.getCurrentFilename());
         return ResponseEntity.ok(renamedFile);
      } else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body("Invalid auth token");
      }

   }

}