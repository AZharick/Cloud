package com.example.cloud.controller;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.User;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private AuthenticationService authenticationService;
   private final PasswordEncoder pswEncoder;
   private final AuthorityRepository authorityRepository;

   public CloudController(UserService userService, FileService fileService, AuthenticationService authenticationService,
                          PasswordEncoder pswEncoder, AuthorityRepository authorityRepository) {
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

   @GetMapping("/common")
   public String common() {
      return "<h1>This is Cloud Controller common page</h1>";
   }

   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody Login login) {
      if (userService.existsUserByUsername(login.getUsername())) {
         return new ResponseEntity<>("This username is already taken!", HttpStatus.BAD_REQUEST);
      }

      Set<Authority> authSet = new HashSet<>(Collections.singletonList(authorityRepository.findById(1)));
      User user = User.builder()
              .username(login.getUsername())
              .password(pswEncoder.encode(login.getPassword()))
              .authorities(authSet)
              .build();
      userService.save(user);

      return new ResponseEntity<>("Registration successful!", HttpStatus.OK);
   }

   //requestBody: JSON: String login, String passwordHash
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody Login login) {
      Authentication auth = authenticationService.authenticate(
              new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(auth);
      return new ResponseEntity<>("Success authorization!", HttpStatus.OK);
   }

   @PostMapping("/logout")
   public void logout() {
      //in: header String "auth-token"
      //удаляет/деактивирует токен
   }

   @PostMapping("/file")
   public void uploadFile() {
      //in: header String "auth-token"
      //in query - String filename
      //requestBody: multipart/form-data: File
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
   public void getAllFiles() {
      //in: header String "auth-token"
      //in query - integer numOfItems
   }

}