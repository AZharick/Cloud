package com.example.cloud.controller;

import com.example.cloud.domain.Login;
import com.example.cloud.service.FileService;
import com.example.cloud.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CloudController {
   private final UserService userService;
   private final FileService fileService;

   public CloudController(UserService userService, FileService fileService) {
      this.userService = userService;
      this.fileService = fileService;
   }

   /* FRONT-приложение использует header auth-token, в котором отправляет токен (ключ-строка) для идентификации
   пользователя на BACKEND. Для получения токена нужно пройти авторизацию на BACKEND и отправить на метод /login
    логин и пароль. В случае успешной проверки в ответ BACKEND должен вернуть json-объект с полем auth-token и значением
     токена. Все дальнейшие запросы с FRONTEND, кроме метода /login, отправляются с этим header.*/

   @GetMapping("/common")
   public String common(){
      return "<h1>This is Cloud Controller common page</h1>";
   }

   @GetMapping("/admin")
   public String admin(){
      return "<h1>This is Admin-only test page</h1>";
   }

   //requestBody: JSON: String login, String passwordHash
   @PostMapping("/login")
   public String login(Login login) {
      return login.toString();
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