package com.example.user.dto;

public class LoginRequest {
    
    @jakarta.validation.constraints.NotBlank
    private String usernameOrEmail;
    
    @jakarta.validation.constraints.NotBlank
    private String password;

    // Getters and setters
    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
