package com.example.cloud.util;

public class PasswordConcealer {

   public static String conceal(String password) {
      int charQuantity = password.length();
      return "*".repeat(charQuantity);
   }

}
