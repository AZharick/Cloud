package com.example.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.naming.AuthenticationException;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Error extends Exception {
   private String message;
   private int id;
}