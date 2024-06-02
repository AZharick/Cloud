package com.example.cloud.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Login {

   @JsonProperty("auth-token")
   private String authToken;

}