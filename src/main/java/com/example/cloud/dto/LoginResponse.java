package com.example.cloud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
public class LoginResponse {

   @JsonProperty("auth-token")
   private String authToken;

}