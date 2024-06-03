package com.medsys.medsysapi.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public enum SecUserRoles {
    //id : 1
    ROLE_USER("ROLE_USER"),
    //id : 2
    ROLE_DOCTOR("ROLE_DOCTOR"),
    //id : 3
    ROLE_PERSONNEL("ROLE_PERSONNEL"),
    //id : 4
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    SecUserRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }

    public static int getId(String role) {
        switch (role) {
            case "ROLE_DOCTOR":
                return 2;
            case "ROLE_PERSONNEL":
                return 3;
            case "ROLE_ADMIN":
                return 4;
            default:
                if (role.equals("ROLE_USER")) {
                    return 1;
                } else {
                    throw new IllegalArgumentException("Invalid role: " + role);
                }
        }
    }

    public static int getId(Set<GrantedAuthority> authorities) {
        int id = 0;
        for (GrantedAuthority grantedAuthority : authorities) {
            int temp = getId(grantedAuthority.getAuthority());
            if (temp > id) {
                id = temp;
            }
        }
        if(id == 0) {
            throw new IllegalArgumentException("User has no role.");
        }
        return id;
    }
}
