package com.rental.system.security;

import com.rental.system.user.Staff;
import com.rental.system.repository.StaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("null")
public class StaffUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${app.root.username:admin_root}")
    private String rootUsername;

    @org.springframework.beans.factory.annotation.Value("${app.root.password:#{null}}")
    private String rootPassword;

    public StaffUserDetailsService(StaffRepository staffRepository, @org.springframework.context.annotation.Lazy org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Intercept Virtual Root User directly for Spring Security
        if (username.equals(rootUsername)) {
            if (rootPassword == null || rootPassword.isBlank()) {
                throw new IllegalStateException("CRITICAL SECURITY ERROR: Root password missing!");
            }
            Staff root = new Staff("System Root", rootUsername, passwordEncoder.encode(rootPassword), 0.0) {
                @Override
                public boolean can(String action) { return true; }
            };
            root.setStaffId(-1);
            root.setActive(true);
            root.setStatus(true);
            return new StaffPrincipal(root);
        }

        Staff staff = staffRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found with username: " + username));
        return new StaffPrincipal(staff);
    }
}
