package com.example.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES", schema = "HR")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(
        name = "role_seq",
        sequenceName = "ROLE_SEQ",
        allocationSize = 1
    )
    @Column(name = "ROLEID", nullable = false)
    private Long roleId;

    @Column(name = "ROLENAME", nullable = false, unique = true)
    private String roleName;

    // ✅ REQUIRED by JPA
    public Role() {}

    public Role(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
