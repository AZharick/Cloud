package com.example.cloud.util;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PasswordConcealerTests {

   @Test
   public void testConceal() {
      final String test = "test";
      final String expected = "****";
      final String actual = PasswordConcealer.conceal(test);
      Assertions.assertEquals(expected, actual);
   }

}