package com.example.user.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.user.entity.User;
import com.example.user.entity.Supplier;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private final Long userId;        // ADMIN / USER
    private final Long supplierId;    // SUPPLIER
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(
            Long userId,
            Long supplierId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.supplierId = supplierId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /* =====================================================
       FACTORY METHODS
       ===================================================== */

    // ✅ Admin / User (from DB)
    public static UserPrincipal create(User user) {
        String roleName = user.getRole() != null
                ? user.getRole().getRoleName()
                : "USER";

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new UserPrincipal(
                user.getId(),
                null,
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    // ✅ Supplier (from DB)
    public static UserPrincipal create(Supplier supplier) {
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_SUPPLIER"));

        return new UserPrincipal(
                null,
                supplier.getSupplierId(),
                supplier.getSuppliersEmail(),
                supplier.getPassword(),
                authorities
        );
    }

    // ✅ JWT (NO DB HIT)
    public static UserPrincipal createFromJwt(
            Long supplierId,
            String username,
            String role
    ) {
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(role));

        if ("ROLE_SUPPLIER".equals(role)) {
            return new UserPrincipal(
                    null,
                    supplierId,
                    username,
                    null,
                    authorities
            );
        }

        // ADMIN / USER
        return new UserPrincipal(
                supplierId,   // can be userId if you add it to JWT later
                null,
                username,
                null,
                authorities
        );
    }

    /* =====================================================
       HELPERS
       ===================================================== */

    public Long getSupplierId() {
        return isSupplier() ? supplierId : null;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isSupplier() {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPLIER"));
    }

    public boolean isAdmin() {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /* =====================================================
       UserDetails
       ===================================================== */

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
