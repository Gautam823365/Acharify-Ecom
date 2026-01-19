package com.example.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.user.config.JwtTokenProvider;
import com.example.user.config.UserPrincipal;
import com.example.user.dto.EditProfileRequest;
import com.example.user.dto.LoginRequest;
import com.example.user.dto.SignUpRequest;
import com.example.user.entity.Role;
import com.example.user.entity.Supplier;
import com.example.user.entity.User;
import com.example.user.repo.RoleRepository;
import com.example.user.repo.SupplierRepository;
import com.example.user.repo.UserRepository;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private SupplierRepository supplierRepository;

    
    public AuthController(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
this.roleRepository = roleRepository;
this.userRepository = userRepository;
this.passwordEncoder = passwordEncoder;
}
    // ------------------- SIGN IN -------------------
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        // ✅ Get authenticated user safely
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", jwt);
        response.put("tokenType", "Bearer");
        response.put("username", userPrincipal.getUsername());
        response.put("role", userPrincipal.getAuthorities()
                .iterator()
                .next()
                .getAuthority()); // ROLE_USER / ROLE_ADMIN / ROLE_SUPPLIER

        // ✅ Add supplierId if user is a supplier
        if ("ROLE_SUPPLIER".equals(response.get("role"))) {
            // fetch supplier by email (or username)
            Supplier supplier = supplierRepository
                .findBySuppliersEmail(userPrincipal.getUsername()) // method based on Supplier entity
                .orElse(null);

            if (supplier != null) {
                response.put("supplierId", supplier.getSupplierId());
            } else {
                response.put("supplierId", null);
            }
        }

        return ResponseEntity.ok(response);
    }

    // ------------------- GET CURRENT USER -------------------
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Unauthorized"));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getFullName());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("phoneNumber", user.getPhoneNumber());
        response.put("profileImage", user.getProfileImage());
        response.put("address", user.getAddress());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam("file") MultipartFile file) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Unauthorized"));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
        }

        try {
            // Save file in backend "uploads" folder
            String uploadDir = "uploads";   // relative to backend root
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());

            // URL exposed by Spring Boot
            String imageUrl = "/uploads/" + fileName;

            user.setProfileImage(imageUrl);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile image uploaded successfully");
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Failed to upload image"));
        }
    }

 // ------------------- EDIT PROFILE -------------------
    @PutMapping("/me")
    public ResponseEntity<?> editProfile(@AuthenticationPrincipal UserDetails userDetails,
                                         @Valid @RequestBody EditProfileRequest editProfileRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "Unauthorized"));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
        }

        // Update fields
        if (editProfileRequest.getFullName() != null) {
            user.setFullName(editProfileRequest.getFullName());
        }
        if (editProfileRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(editProfileRequest.getPhoneNumber());
        }
        if (editProfileRequest.getProfileImage() != null) {
            user.setProfileImage(editProfileRequest.getProfileImage());
        }
        if (editProfileRequest.getEmail() != null && !editProfileRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(editProfileRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already in use!"));
            }
            user.setEmail(editProfileRequest.getEmail());
        }

        // Handle Address (DTO -> Entity mapping)
        if (editProfileRequest.getAddress() != null) {
            com.example.user.dto.Address reqAddr = editProfileRequest.getAddress(); // DTO
            com.example.user.entity.Address userAddr = user.getAddress() != null 
                ? user.getAddress() 
                : new com.example.user.entity.Address();

            if (reqAddr.getStreet() != null) userAddr.setStreet(reqAddr.getStreet());
            if (reqAddr.getCity() != null) userAddr.setCity(reqAddr.getCity());
            if (reqAddr.getState() != null) userAddr.setState(reqAddr.getState());
            if (reqAddr.getZip() != null) userAddr.setZip(reqAddr.getZip());

            user.setAddress(userAddr);
        }

        User updatedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile updated successfully");
        response.put("name", updatedUser.getFullName());
        response.put("username", updatedUser.getUsername());
        response.put("email", updatedUser.getEmail());
        response.put("phoneNumber", updatedUser.getPhoneNumber());
        response.put("profileImage", updatedUser.getProfileImage());
        response.put("address", updatedUser.getAddress());

        return ResponseEntity.ok(response);
    }

    // ------------------- SIGN UP -------------------
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest request) {

        Role userRole = roleRepository.findByRoleName("USER")
            .orElseThrow(() -> new RuntimeException("ROLE USER not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getMobile());
        user.setRole(userRole);      // ✅ THIS FIXES EVERYTHING
        user.setIsActive(true);

        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
    
    // ------------------- API RESPONSE CLASS -------------------
    public static class ApiResponse {
        private Boolean success;
        private String message;

        public ApiResponse(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public Boolean getSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
