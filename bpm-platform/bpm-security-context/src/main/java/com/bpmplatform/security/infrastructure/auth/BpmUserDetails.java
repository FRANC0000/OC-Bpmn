package com.bpmplatform.security.infrastructure.auth;

import com.bpmplatform.security.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record BpmUserDetails(User user) implements UserDetails {

    public UUID getUserId() { return user.getId(); }
    public UUID getTenantId() { return user.getTenantId(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getPrimaryRole() == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getPrimaryRole().getName().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return !"locked".equals(user.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return "active".equals(user.getStatus());
    }
}
