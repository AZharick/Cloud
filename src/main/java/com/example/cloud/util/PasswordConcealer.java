package com.example.cloud.util;

public class PasswordConcealer {

   public static String conceal (String password) {
      int charQuantity = password.length();
      StringBuilder concealedPassword = new StringBuilder();
      for (int i = 0; i < charQuantity; i++) {
         concealedPassword.append('*');
      }
      return concealedPassword.toString();
   }

}
