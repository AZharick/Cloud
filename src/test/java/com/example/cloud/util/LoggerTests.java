package com.example.cloud.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerTests {

   private static final String LOG_FILE_PATH = "./log.txt";

   @BeforeEach
   public void initLog() throws IOException {
      File logFile = new File(LOG_FILE_PATH);
      if (logFile.exists()) {
         logFile.delete();
      }
   }

   @AfterEach
   public void deleteLog() {
      File logFile = new File(LOG_FILE_PATH);
      if (logFile.exists()) {
         logFile.delete();
      }
   }

   @Test
   public void testInitLogCreatesNewFile() throws IOException {
      assertFalse(new File(LOG_FILE_PATH).exists(), "Log file should not exist before initLog is called");
      Logger.initLog();

      assertTrue(new File(LOG_FILE_PATH).exists(), "Log file should be created after initLog is called");
   }

   @Test
   public void testInitLogDeletesExistingFile() throws IOException {
      File logFile = new File(LOG_FILE_PATH);
      logFile.createNewFile();

      assertTrue(logFile.exists(), "Log file should exist before initLog is called");

      Logger.initLog();

      assertTrue(logFile.exists(), "Log file should be created after initLog is called");
   }

   @Test
   public void testLogYellow() throws IOException {
      String testMessage = "Yellow log message";
      Logger.logYellow(testMessage);

      assertTrue(verifyLogRecord(testMessage), "Yellow log message should be written to the log file");
   }

   @Test
   public void testLogGreen() throws IOException {
      String testMessage = "Green log message";
      Logger.logGreen(testMessage);

      assertTrue(verifyLogRecord(testMessage), "Green log message should be written to the log file");
   }

   @Test
   public void testLogRed() throws IOException {
      String testMessage = "Red log message";
      Logger.logRed(testMessage);

      assertTrue(verifyLogRecord(testMessage), "Red log message should be written to the log file");
   }

   private boolean verifyLogRecord(String expectedMessage) throws IOException {
      try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
         String line;
         while ((line = reader.readLine()) != null) {
            if (line.contains(expectedMessage)) {
               return true;
            }
         }
      }
      return false;
   }

}