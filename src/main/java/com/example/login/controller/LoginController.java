package com.example.login.controller;

import com.example.login.model.dto.UserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/loginUser")
  public String loginUser(@RequestBody UserLoginDto loginDto) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginDto.getUsername(),
              loginDto.getPassword()
          )
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return "Login successful";
    } catch (AuthenticationException e) {
      return "Login failed: " + e.getMessage();
    }
  }
}

