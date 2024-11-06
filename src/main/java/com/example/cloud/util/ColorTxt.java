package com.example.cloud.util;

public class ColorTxt {
   public static final String ANSI_GREEN = "\u001b[32;1m";
   public static final String ANSI_YELLOW = "\u001b[33;1m";
   public static final String ANSI_RED = "\u001b[31;1m";
   public static final String ANSI_RESET = "\u001B[0m";

   public static void writeInYellow(String inputText) {
      System.out.println(ANSI_YELLOW + inputText + ANSI_RESET);
   }

   public static void writeInGreen(String inputText) {
      System.out.println(ANSI_GREEN + inputText + ANSI_RESET);
   }

   public static void writeInRed(String inputText) {
      System.out.println(ANSI_RED + inputText + ANSI_RESET);
   }

}