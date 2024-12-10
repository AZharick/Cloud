package com.example.cloud.util;

public class PasswordConcealer {

   public static String conceal(String password) {
      int charQuantity = password.length();
      StringBuilder concealedPassword = new StringBuilder();
      concealedPassword.append("*".repeat(charQuantity));
      return concealedPassword.toString();
   }

}
