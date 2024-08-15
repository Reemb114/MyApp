package com.example.login.service;

import com.example.login.model.dto.PasswordChangeDto;
import com.example.login.model.User;
import com.example.login.model.enums.Role;
import com.example.login.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Optional<User> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User registerNewUser(String username, String password, Set<Role> roles) {
    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setRoles(roles != null && !roles.isEmpty() ? roles : Set.of(Role.USER));
    newUser.setCreatedAt(LocalDateTime.now());
    newUser.setLastPasswordChangeAt(LocalDateTime.now());
    return userRepository.save(newUser);
  }

  public void changePassword(String username, PasswordChangeDto passwordChangeDto) throws Exception {
    User user = findUserByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
      throw new Exception("Old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
    user.setLastPasswordChangeAt(LocalDateTime.now());
    userRepository.save(user);
  }
}

