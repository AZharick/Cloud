package com.example.cloud.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebConfigTests {

   private final MockMvc mockMvc;

   @Autowired
   public WebConfigTests(MockMvc mockMvc) {
      this.mockMvc = mockMvc;
   }

   @Test
   public void testCorsConfiguration() throws Exception {
      mockMvc.perform(options("/login")
                      .header(HttpHeaders.ORIGIN, "http://localhost:8081"))
              .andExpect(status().isOk())
              .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:8081"))
              .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
              .andExpect(header().string("Access-Control-Allow-Methods", "*"));
   }

   @Test
   public void testCorsConfigurationWithDifferentOrigin() throws Exception {
      mockMvc.perform(options("/login")
                      .header(HttpHeaders.ORIGIN, "http://github.com"))
              .andExpect(status().isOk())
              .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
   }
}
