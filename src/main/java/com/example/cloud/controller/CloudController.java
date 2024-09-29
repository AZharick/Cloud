package com.example.cloud.controller;

import com.example.cloud.domain.*;
import com.example.cloud.domain.Error;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import com.example.cloud.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private AuthenticationService authenticationService;
   private final PasswordEncoder pswEncoder;
   private final AuthorityRepository authorityRepository;

   public CloudController(UserService userService, FileService fileService, AuthenticationService authenticationService,
                          PasswordEncoder pswEncoder, AuthorityRepository authorityRepository
   ) {
      this.userService = userService;
      this.fileService = fileService;
      this.authenticationService = authenticationService;
      this.pswEncoder = pswEncoder;
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

//   @PostMapping
//   public ResponseEntity<String> uploadFile(
//           @RequestHeader("auth-token") String authToken,
//           @RequestParam("filename") String filename,
//           @RequestParam("file") MultipartFile file) {
//
//      // Проверка токена аутентификации
//      if (!isTokenValid(authToken)) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
//      }
//
//      // Логика загрузки файла на сервер
//      try {
//         // Например, сохранить файл на диск
//         saveFile(file, filename);
//         return ResponseEntity.ok("File uploaded successfully");
//      } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
//      }
//   }

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

//   @GetMapping("/list")
//   public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
//
//      List<File> userFiles = new ArrayList<>();
//      if (authToken.equals(this.authToken)) {
//         userFiles = fileService.findAllByUserId((long) userService.getUserIdByToken(authToken));
//      }
//      return new ResponseEntity<>(userFiles.subList(0, limit - 1), HttpStatus.OK);
//   }

}