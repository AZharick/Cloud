package com.example.cloud.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class TokenGenerator {

   public static String generateUUIDToken() {
      SecureRandom secureRandom;
      try {
         secureRandom = SecureRandom.getInstanceStrong();
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
         return null;
      }

      byte[] randomBytes = new byte[32];
      secureRandom.nextBytes(randomBytes);
      UUID uuid = UUID.nameUUIDFromBytes(randomBytes);
      return uuid.toString();
   }

}