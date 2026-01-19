package com.example.user.dto;

//LoginResponseDTO.java
public class LoginResponseDTO {
 private String accessToken;
 private String tokenType;
 private String role;
 private String username;
 private Long supplierId; // <-- add this

 // Constructors
 public LoginResponseDTO(String accessToken, String tokenType, String role, String username, Long supplierId) {
     this.accessToken = accessToken;
     this.tokenType = tokenType;
     this.role = role;
     this.username = username;
     this.supplierId = supplierId;
 }

 // Getters and setters
 public String getAccessToken() { return accessToken; }
 public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

 public String getTokenType() { return tokenType; }
 public void setTokenType(String tokenType) { this.tokenType = tokenType; }

 public String getRole() { return role; }
 public void setRole(String role) { this.role = role; }

 public String getUsername() { return username; }
 public void setUsername(String username) { this.username = username; }

 public Long getSupplierId() { return supplierId; }
 public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
}
