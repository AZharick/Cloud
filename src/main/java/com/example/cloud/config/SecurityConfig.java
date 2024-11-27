package com.example.cloud.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable).
              authorizeHttpRequests(auth -> {
                 auth.requestMatchers("/login").permitAll();
                 auth.requestMatchers("/list", "/logout").hasAuthority("full");
                 auth.anyRequest().authenticated();
              });
      return http.build();
   }

}