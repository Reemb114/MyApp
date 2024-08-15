package com.example.login.service;

import com.example.login.model.User;
import com.example.login.model.dto.PasswordChangeDto;
import com.example.login.model.enums.Role;
import com.example.login.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterNewUser() {
    String username = "testUser";
    String password = "password123";
    Set<Role> roles = Set.of(Role.USER);

    User user = new User();
    user.setUsername(username);
    user.setPassword("encodedPassword");
    user.setRoles(roles);

    when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    User createdUser = userService.registerNewUser(username, password, roles);

    assertNotNull(createdUser);
    assertEquals("testUser", createdUser.getUsername());
    assertEquals("encodedPassword", createdUser.getPassword());
    assertTrue(createdUser.getRoles().contains(Role.USER));

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testChangePassword() throws Exception {
    String username = "testUser";
    String oldPassword = "oldPassword";
    String newPassword = "newPassword";

    User user = new User();
    user.setUsername(username);
    user.setPassword("encodedOldPassword");

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

    PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
    passwordChangeDto.setOldPassword(oldPassword);
    passwordChangeDto.setNewPassword(newPassword);

    userService.changePassword(username, passwordChangeDto);

    assertEquals("encodedNewPassword", user.getPassword());
    verify(userRepository, times(1)).save(user);
  }


  @Test
  void testChangePasswordWrongOldPassword() {
    String username = "testUser";
    String oldPassword = "wrongOldPassword";
    String newPassword = "newPassword";

    User user = new User();
    user.setUsername(username);
    user.setPassword("encodedOldPassword");

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

    PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
    passwordChangeDto.setOldPassword(oldPassword);
    passwordChangeDto.setNewPassword(newPassword);

    assertThrows(Exception.class, () -> userService.changePassword(username, passwordChangeDto));
  }
}
