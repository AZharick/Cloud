package com.example.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

   @Bean
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf().disable().authorizeHttpRequests(auth -> {
         auth.requestMatchers("/file").hasRole("ADMIN");
         auth.anyRequest().authenticated();
      }).httpBasic(Customizer.withDefaults());
      return http.build();
   }

   @Bean
   UserDetailsManager users(DataSource dataSource) {
      UserDetails admin = User.builder()
              .username("admin")
              .password(passwordEncoder().encode("admin"))
              .roles("ADMIN")
              .build();

      JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
      if (!users.userExists("admin")) {
         users.createUser(admin);
      }
      return users;
   }

}