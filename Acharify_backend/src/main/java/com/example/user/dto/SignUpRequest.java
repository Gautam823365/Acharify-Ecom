package com.example.user.dto;

public class SignUpRequest {
    
    @jakarta.validation.constraints.NotBlank
    private String username;
    
    @jakarta.validation.constraints.NotBlank
    private String password;
    
    @jakarta.validation.constraints.Email
    @jakarta.validation.constraints.NotBlank
    private String email;
    
    private String fullName;
    
    private String mobile;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}

