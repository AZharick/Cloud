package com.example.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean //*
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean //*
   SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable).
              authorizeHttpRequests(auth -> {
                 auth.requestMatchers("/common").permitAll();
                 auth.requestMatchers("/login").hasAuthority("full");
                 auth.anyRequest().authenticated();
              }).httpBasic(Customizer.withDefaults());
      return http.build();
   }

   @Bean //*
   UserDetailsManager userDetailsManager(DataSource dataSource) {
      UserDetails admin = User.builder()
              .username("admin")
              .password(passwordEncoder().encode("admin"))
              .authorities("full")
              .build();

      JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
      if (!users.userExists("admin")) {
         users.createUser(admin);
      }
      return users;
   }

//   @Bean  //n
//   public AuthenticationManager authenticationManagerBean() throws Exception {
//      List<GrantedAuthority> authorities = new ArrayList<>();
//      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//
//      UserDetails userDetails = User.builder()
//              .username("username")
//              .password(passwordEncoder().encode("password"))
//              .authorities(authorities)
//              .build();
//
//      InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(userDetails);
//
//      ProviderManager providerManager = new ProviderManager(Arrays.asList(
//              new DaoAuthenticationProvider() {{
//                 setUserDetailsPasswordService(userDetailsManager);
//                 setPasswordEncoder(passwordEncoder());
//              }}
//      ));
//
//      return providerManager;
//   }

}