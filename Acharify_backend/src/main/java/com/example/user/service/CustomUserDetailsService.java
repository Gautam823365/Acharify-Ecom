package com.example.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user.config.UserPrincipal;
import com.example.user.entity.User;
import com.example.user.repo.UserRepository;
import com.example.user.repo.SupplierRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                    SupplierRepository supplierRepository) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // Try Admin/User by email
    	return userRepository.findByEmail(usernameOrEmail)
    	        .map(UserPrincipal::create)
    	        .orElseGet(() ->
    	            userRepository.findByUsername(usernameOrEmail)
    	                .map(UserPrincipal::create)
    	                .orElseGet(() ->
    	                    supplierRepository.findBySuppliersEmail(usernameOrEmail)
    	                        .map(UserPrincipal::create)  // ✅ now works
    	                        .orElseThrow(() ->
    	                            new UsernameNotFoundException("User not found: " + usernameOrEmail)
    	                        )
    	                )
    	        );

    }
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id: " + id)
                );
        return UserPrincipal.create(user);
    }
}
