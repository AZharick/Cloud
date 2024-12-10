package com.example.cloud.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenGeneratorTests {

      @Test
      public void testGenerateUUIDToken() {
         String token = TokenGenerator.generateUUIDToken();
         assertTrue(isValidUUID(token), "Token should be a valid UUID");
      }

      private boolean isValidUUID(String uuid) {
         try {
            UUID.fromString(uuid);
            return true;
         } catch (IllegalArgumentException e) {
            return false;
         }
      }

}