package com.example.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.repo.RoleRepository;
import com.example.user.repo.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdminService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String[] roleNames = {
            "ADMIN",
            "PROCUREMENT_OFFICER",
            "PRODUCTION_MANAGER",
            "INVENTORY_MANAGER",
            "SUPPLIER"
        };

        Map<String, Role> roles = new HashMap<>();

        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName(roleName);
                    return roleRepository.save(r);
                });
            roles.put(roleName, role);
        }

        // Remove existing admin (if any)
        userRepository.findByUsername("admin")
            .ifPresent(userRepository::delete);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setPhoneNumber("8096845632");
        admin.setIsActive(true);
        admin.setRole(roles.get("ADMIN"));

        userRepository.save(admin);

        System.out.println("✅ Admin user and roles initialized successfully");
    }
}
