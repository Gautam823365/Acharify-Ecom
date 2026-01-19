package com.example.user.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "USERS", schema = "HR")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "USERID", nullable = false)
    private Long id;

    @Column(name = "ISACTIVE")
    private Boolean isActive;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "USERPASSWORD", nullable = false)
    private String password;

    @Column(name = "USERFULLNAME")
    private String fullName;

    @Column(name = "USEREMAIL", unique = true)
    private String email;

    @Column(name = "USERMOBILE")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USERROLEID", referencedColumnName = "ROLEID", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USERADDRESSID", referencedColumnName = "ADDRESSID")
    private Address address;

    @Column(name = "USERPROFILEIMG")
    private String profileImage;

    // getters & setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
