package com.example.cloud.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CloudLogger {
   private static final Logger logger = Logger.getLogger(CloudLogger.class.getName());
   FileHandler fileHandler = new FileHandler("cloud.log", true);

   public CloudLogger() throws IOException {
      logger.setLevel(Level.ALL);
      logger.addHandler(fileHandler);
      fileHandler.setFormatter(new SimpleFormatter());
   }

   public static void logInfo(String message) {
      logger.info(message);
   }

   public static void logWarning(String message) {
      logger.warning(message);
   }

   public static void logSevere(String message) {
      logger.severe(message);
   }

}