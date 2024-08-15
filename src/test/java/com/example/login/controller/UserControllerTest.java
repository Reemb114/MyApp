package com.example.login.controller;

import com.example.login.model.dto.PasswordChangeDto;
import com.example.login.model.dto.UserRegistrationDto;
import com.example.login.model.enums.Role;
import com.example.login.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterUser() {
    UserRegistrationDto registrationDto = new UserRegistrationDto();
    registrationDto.setUsername("testUser");
    registrationDto.setPassword("password123");
    registrationDto.setConfirmPassword("password123");

    Set<Role> expectedRoles = Set.of(Role.USER);

    String result = userController.registerUser(registrationDto);

    assertEquals("User registered successfully!", result);
    verify(userService, times(1)).registerNewUser("testUser", "password123", expectedRoles);
  }


  @Test
  void testRegisterUserPasswordsDoNotMatch() {
    UserRegistrationDto registrationDto = new UserRegistrationDto();
    registrationDto.setUsername("testUser");
    registrationDto.setPassword("password123");
    registrationDto.setConfirmPassword("wrongPassword");

    String result = userController.registerUser(registrationDto);

    assertEquals("Passwords do not match", result);
    verify(userService, never()).registerNewUser(anyString(), anyString(), anySet());
  }

  @Test
  void testChangePassword() throws Exception {
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("testUser");

    PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
    passwordChangeDto.setOldPassword("oldPassword");
    passwordChangeDto.setNewPassword("newPassword");

    when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

    String result = userController.changePassword(passwordChangeDto, userDetails);

    assertEquals("Password changed successfully", result);
    verify(userService, times(1)).changePassword("testUser", passwordChangeDto);
  }

  @Test
  void testChangePasswordNotAuthenticated() throws Exception {
    PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
    passwordChangeDto.setOldPassword("oldPassword");
    passwordChangeDto.setNewPassword("newPassword");

    String result = userController.changePassword(passwordChangeDto, null);

    assertEquals("User is not authenticated", result);
    verify(userService, never()).changePassword(anyString(), any(PasswordChangeDto.class));
  }
}
