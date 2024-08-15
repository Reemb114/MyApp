package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @DeleteMapping("/users/{id}")
  public String deleteUser(@PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      User adminUser = adminService.findUserByUsername(userDetails.getUsername())
          .orElseThrow(() -> new Exception("Admin user not found"));
      adminService.deleteUser(id, adminUser);
      return "User deleted successfully";
    } catch (Exception e) {
      return "Deletion failed: " + e.getMessage();
    }
  }
}