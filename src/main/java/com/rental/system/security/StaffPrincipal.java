package com.rental.system.security;

import com.rental.system.user.Staff;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("null")
public class StaffPrincipal implements UserDetails {
    private final Staff staff;

    public StaffPrincipal(Staff staff) {
        this.staff = staff;
    }

    public Staff getStaff() {
        return staff;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Virtual ROOT bypasses all method-level security rules
        if ("ROOT".equals(staff.getRole())) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ROOT"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"),
                    new SimpleGrantedAuthority("ROLE_REGULAR"),
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        }
        String role = staff.getClass().getSimpleName().replace("Staff", "").toUpperCase();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return staff.getPassword();
    }

    @Override
    public String getUsername() {
        return staff.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return staff.getStatus();
    }
}
