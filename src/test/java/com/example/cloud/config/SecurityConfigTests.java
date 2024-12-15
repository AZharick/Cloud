package com.example.cloud.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

   @Autowired
   private MockMvc mockMvc;

   @Test
   public void testLoginPageIsPublic() throws Exception {
      mockMvc.perform(get("/login"))
              .andExpect(status().isOk());
   }

   @Test
   @WithMockUser(authorities = "full")
   public void testListPageIsAccessibleForFullAuthority() throws Exception {
      mockMvc.perform(get("/list"))
              .andExpect(status().isOk());
   }

   @Test
   @WithMockUser(authorities = "full")
   public void testLogoutPageIsAccessibleForFullAuthority() throws Exception {
      mockMvc.perform(get("/logout"))
              .andExpect(status().isOk());
   }

   @Test
   public void testListPageIsForbiddenForAnonymousUser() throws Exception {
      mockMvc.perform(get("/list"))
              .andExpect(status().isForbidden());
   }

   @Test
   public void testAnyOtherPageRequiresAuthentication() throws Exception {
      mockMvc.perform(get("/some-other-page"))
              .andExpect(status().isUnauthorized());
   }
}
