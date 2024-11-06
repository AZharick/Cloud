package com.example.cloud.controller;

import com.example.cloud.domain.*;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import static com.example.cloud.util.ColorTxt.writeInYellow;
import static com.example.cloud.util.ColorTxt.writeInGreen;
import static com.example.cloud.util.ColorTxt.writeInRed;

@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private AuthenticationService authenticationService;
   private final AuthorityRepository authorityRepository;

   public CloudController(UserService userService, FileService fileService, AuthenticationService authenticationService,
                          PasswordEncoder pswEncoder, AuthorityRepository authorityRepository
   ) {
      this.userService = userService;
      this.fileService = fileService;
      this.authenticationService = authenticationService;
      this.authorityRepository = authorityRepository;
   }

   /* FRONT-приложение использует header auth-token, в котором отправляет токен (ключ-строка) для идентификации
   пользователя на BACKEND. Для получения токена нужно пройти авторизацию на BACKEND и отправить на метод /login
    логин и пароль. В случае успешной проверки в ответ BACKEND должен вернуть json-объект с полем auth-token и значением
     токена. Все дальнейшие запросы с FRONTEND, кроме метода /login, отправляются с этим header.*/

//   @PostMapping("/register")
//   public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest) {
//
//      if (userService.existsUserByUsername(loginRequest.getLogin())) {
//         return new ResponseEntity<>("This username is already taken!", HttpStatus.BAD_REQUEST);
//      }
//
//      Set<Authority> authSet = new HashSet<>(Collections.singletonList(authorityRepository.findById(1)));
//      User user = User.builder()
//              .username(loginRequest.getLogin())
//              .password(pswEncoder.encode(loginRequest.getPassword()))
//              .authorities(authSet)
//              .build();
//      userService.save(user);
//
//      return new ResponseEntity<>("Registration successful!", HttpStatus.OK);
//   }

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
           @RequestParam("file") MultipartFile file){

      writeInYellow("> UPLOAD ATTEMPT:");

      if (!authenticationService.isTokenValid(authToken)) {
         writeInRed("> Invalid auth token");
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
      }

      writeInGreen("> Checkout successful!");
      try {
         fileService.save(authToken, file, filename);
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

   //result req: JSON-Object w\filename'n'filesize
   @GetMapping("/list")
   public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
      List<File> userFiles = new ArrayList<>();
      long userId = userService.getUserIdByToken(authToken);

      if (authenticationService.isTokenValid(authToken)) {
         userFiles = fileService.getFilesInQtyOf(limit, userId);
      }

      return new ResponseEntity<>(userFiles.subList(0, limit - 1), HttpStatus.OK);
   }

}