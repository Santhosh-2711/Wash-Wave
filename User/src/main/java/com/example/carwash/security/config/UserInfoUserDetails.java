package com.example.carwash.security.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.carwash.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
public class UserInfoUserDetails implements UserDetails {

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;
    
    public UserInfoUserDetails(User user) {
        //User user = optional.orElseThrow(() -> new IllegalArgumentException("User not found")); 
        this.name = user.getUsername();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
        return true;
    }
}
