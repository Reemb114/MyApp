package com.example.login.service;

import com.example.login.model.User;
import com.example.login.model.enums.Role;
import com.example.login.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  private final UserRepository userRepository;

  @Autowired
  public AdminService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void deleteUser(Long userId, User adminUser) throws Exception {
    if (!adminUser.getRoles().contains(Role.ADMIN)) {
      throw new Exception("Only admins can delete users");
    }
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new Exception("User not found"));
    userRepository.delete(user);
  }

  public Optional<User> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
