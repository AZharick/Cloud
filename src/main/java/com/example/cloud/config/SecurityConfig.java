package com.example.cloud.config;

import com.example.cloud.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final CustomUserDetailsService customUserDetailsService;

   @Autowired
   public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
      this.customUserDetailsService = customUserDetailsService;
   }

   @Bean //*
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean //*
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable).
              authorizeHttpRequests(auth -> {
                 auth.requestMatchers("/common", "/register", "/login").permitAll();
                 auth.anyRequest().authenticated();
              }).httpBasic(Customizer.withDefaults());
      return http.build();
   }

   @Bean
   AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
   }

}