package com.example.cloud.util;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class DeBearererTest {

   @Test
   public void testDeBearerize_WithBearer() {
      String input = "Bearer myAccessToken123";
      String expected = "myAccessToken123";
      String result = DeBearerer.deBearerize(input);
      assertEquals(expected, result);
   }

   @Test
   public void testDeBearerize_WithoutBearer() {
      String input = "myAccessToken123";
      String expected = "no \"Bearer\" found";
      String result = DeBearerer.deBearerize(input);
      assertEquals(expected, result);
   }

   @Test
   public void testDeBearerize_NullInput() {
      String input = null;
      String expected = "no \"Bearer\" found";
      String result = DeBearerer.deBearerize(input);
      assertEquals(expected, result);
   }

   @Test
   public void testDeBearerize_EmptyString() {
      String input = "";
      String expected = "no \"Bearer\" found";
      String result = DeBearerer.deBearerize(input);
      assertEquals(expected, result);
   }

   @Test
   public void testDeBearerize_SpaceOnly() {
      String input = "     ";
      String expected = "no \"Bearer\" found";
      String result = DeBearerer.deBearerize(input);
      assertEquals(expected, result);
   }
}