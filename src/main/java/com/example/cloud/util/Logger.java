package com.example.cloud.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
   public static final String ANSI_GREEN = "\u001b[32;1m";
   public static final String ANSI_YELLOW = "\u001b[33;1m";
   public static final String ANSI_RED = "\u001b[31;1m";
   public static final String ANSI_RESET = "\u001B[0m";
   private static File logFile;

   public static void initLog() throws IOException {
      logFile = new File(".", "log.txt");
      if(logFile.exists()) {
         logFile.delete();
      }
      logFile.createNewFile();
   }

   public static void logYellow(String inputText) {
      System.out.println(ANSI_YELLOW + inputText + ANSI_RESET);
      writeIntoLogFile(inputText);
   }

   public static void logGreen(String inputText) {
      System.out.println(ANSI_GREEN + inputText + ANSI_RESET);
      writeIntoLogFile(inputText);
   }

   public static void logRed(String inputText) {
      System.out.println(ANSI_RED + inputText + ANSI_RESET);
      writeIntoLogFile(inputText);
   }

   public static void writeIntoLogFile(String text) {
      String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

         try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write("[" + timestamp + "] " + text);
            writer.newLine();
         } catch (IOException e) {
            e.printStackTrace();
         }

   }

}