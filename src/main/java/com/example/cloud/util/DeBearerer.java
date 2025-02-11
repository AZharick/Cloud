package com.example.cloud.util;

import static com.example.cloud.util.CloudLogger.logInfo;

public class DeBearerer {

   public static String deBearerize(String header) {
      logInfo("de-bearerizer initiated!..");
      if (header != null && header.startsWith("Bearer ")) {
         logInfo("HEADER IN: " + header);
         logInfo("HEADER OUT: " + header.substring(7));
         return header.substring(7);
      }
      return "no \"Bearer\" substring found";
   }

}