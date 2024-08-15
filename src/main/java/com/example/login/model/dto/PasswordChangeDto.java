package com.example.login.model.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {

  private String newPassword;
  private String oldPassword;

}