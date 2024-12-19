package com.test.crud.demo.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.crud.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private int id;

    private String username;
    @JsonIgnore
    private String password;

    private String role;

    private String nama;

    public UserDetailsImpl(int id, String username, String password, String role,String nama) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nama = nama;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getName());
    }

    public int getId() {
        return id;
    }
    public String getRole() { return role; }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
