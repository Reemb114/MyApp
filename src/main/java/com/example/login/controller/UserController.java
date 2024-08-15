package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.model.dto.PasswordChangeDto;
import com.example.login.model.dto.UserRegistrationDto;
import com.example.login.model.enums.Role;
import com.example.login.service.UserService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  public String registerUser(@RequestBody UserRegistrationDto registrationDto) {
    if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
      return "Passwords do not match";
    }
    userService.registerNewUser(
        registrationDto.getUsername(),
        registrationDto.getPassword(),
        Set.of(Role.USER)
    );
    return "User registered successfully!";
  }

  @PostMapping("/changePassword")
  public String changePassword(
      @RequestBody PasswordChangeDto passwordChangeDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    if (userDetails == null) {
      return "User is not authenticated";
    }

    try {
      userService.changePassword(userDetails.getUsername(), passwordChangeDto);
      return "Password changed successfully";
    } catch (Exception e) {
      return "Password change failed: " + e.getMessage();
    }
  }
}
