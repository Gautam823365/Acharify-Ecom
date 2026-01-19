package com.example.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class EditProfileRequest {

    @Size(max = 50)
    private String fullName;

    @Size(max = 15)
    private String phoneNumber;

    @Email
    private String email;

    private String profileImage;

    private Address address;  // nested object for address

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
}
