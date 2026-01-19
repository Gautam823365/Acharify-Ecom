package com.example.user.Security;


import com.example.user.entity.Supplier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SupplierUserDetails implements UserDetails {

    private final Supplier supplier;

    public SupplierUserDetails(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_SUPPLIER"));
    }

    @Override
    public String getPassword() {
        return supplier.getPassword();
    }

    @Override
    public String getUsername() {
        return supplier.getSuppliersEmail(); // LOGIN WITH EMAIL
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(supplier.getStatus());
    }
}

