package com.example.cloud.repository;

import com.example.cloud.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserTokenRepository {

   private HashMap<String, User> tokensUsers = new HashMap<>();

   public void putTokenAndUser(String token, User user) {
      tokensUsers.put(token, user);
   }

   public void deleteUserByToken(String token) {
      tokensUsers.remove(token);
   }

   public User getUserByToken(String token) {
      return tokensUsers.get(token);
   }

   public boolean isTokenPresent(String token) {
      return tokensUsers.containsKey(token);
   }

   //TEST METHOD
   public String printTokensAndUsers() {
      StringBuilder mapContents = new StringBuilder();
      mapContents.append("size: ").append(tokensUsers.size()).append("; ");
      for (Map.Entry<String, User> entry : tokensUsers.entrySet()) {
         String token = entry.getKey();
         User user = entry.getValue();
         mapContents.append(token).append(" <=> ").append(user).append("\n");
      }
      return mapContents.toString();
   }


}