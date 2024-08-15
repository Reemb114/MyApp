package com.example.login.service;

import com.example.login.model.User;
import com.example.login.model.enums.Role;
import com.example.login.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdminServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AdminService adminService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testDeleteUserAsAdmin() throws Exception {
    Long userId = 1L;
    User adminUser = new User();
    adminUser.setRoles(Set.of(Role.ADMIN));

    User userToDelete = new User();
    userToDelete.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

    adminService.deleteUser(userId, adminUser);

    verify(userRepository, times(1)).delete(userToDelete);
  }

  @Test
  void testDeleteUserAsNonAdmin() {
    Long userId = 1L;
    User nonAdminUser = new User();
    nonAdminUser.setRoles(Set.of(Role.USER));

    assertThrows(Exception.class, () -> adminService.deleteUser(userId, nonAdminUser));
  }

  @Test
  void testDeleteUserNotFound() {
    Long userId = 1L;
    User adminUser = new User();
    adminUser.setRoles(Set.of(Role.ADMIN));

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> adminService.deleteUser(userId, adminUser));
  }
}
