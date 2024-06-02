package com.example.cloud.controller;

import com.example.cloud.domain.Authority;
import com.example.cloud.domain.Error;
import com.example.cloud.domain.Login;
import com.example.cloud.domain.LoginRequest;
import com.example.cloud.domain.User;
import com.example.cloud.repository.AuthorityRepository;
import com.example.cloud.repository.UserRepository;
import com.example.cloud.service.AuthenticationService;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import com.example.cloud.util.TokenGenerator;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;
   private final UserRepository userRepository;
   private AuthenticationService authenticationService;
   private final PasswordEncoder pswEncoder;
   private final AuthorityRepository authorityRepository;
   private String authToken;

   public CloudController(UserService userService, FileService fileService, AuthenticationService authenticationService,
                          PasswordEncoder pswEncoder, AuthorityRepository authorityRepository, UserRepository userRepository) {
      this.userService = userService;
      this.fileService = fileService;
      this.authenticationService = authenticationService;
      this.pswEncoder = pswEncoder;
      this.authorityRepository = authorityRepository;
      this.userRepository = userRepository;
   }

   /* FRONT-приложение использует header auth-token, в котором отправляет токен (ключ-строка) для идентификации
   пользователя на BACKEND. Для получения токена нужно пройти авторизацию на BACKEND и отправить на метод /login
    логин и пароль. В случае успешной проверки в ответ BACKEND должен вернуть json-объект с полем auth-token и значением
     токена. Все дальнейшие запросы с FRONTEND, кроме метода /login, отправляются с этим header.*/

   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest) {
      if (userService.existsUserByUsername(loginRequest.getLogin())) {
         return new ResponseEntity<>("This username is already taken!", HttpStatus.BAD_REQUEST);
      }

      Set<Authority> authSet = new HashSet<>(Collections.singletonList(authorityRepository.findById(1)));
      User user = User.builder()
              .username(loginRequest.getLogin())
              .password(pswEncoder.encode(loginRequest.getPassword()))
              .authorities(authSet)
              .build();
      userService.save(user);

      return new ResponseEntity<>("Registration successful!", HttpStatus.OK);
   }

   @PostMapping("/login")
   public Object login(@RequestBody LoginRequest loginRequest) {
      try {
         Authentication token = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword());
         Authentication authenticated = authenticationService.authenticate(token);

         if (authenticated.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            System.out.println("AUTH SUCCESS");
         } else throw new Error("Bad credentials", 400);
      } catch (Error e) {
         return e;
      }

      authToken = TokenGenerator.generateUUIDToken();
      Login login = new Login();
      login.setAuthToken(authToken);
      return login;
   }

   @PostMapping("/logout")
   public void logout() {
      //in: header String "auth-token"
      //удаляет/деактивирует токен
      //token.eraseCredentials?
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