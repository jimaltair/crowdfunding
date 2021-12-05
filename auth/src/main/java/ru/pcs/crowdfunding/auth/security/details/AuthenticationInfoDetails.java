package ru.pcs.crowdfunding.auth.security.details;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.Status;

import java.util.Collection;

import java.util.stream.Collectors;

@AllArgsConstructor
public class AuthenticationInfoDetails implements UserDetails {

    private AuthenticationInfo info;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return info.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return info.getPassword();
    }

    @Override
    public String getUsername() {
        return info.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !info.getStatus().getName().equals(Status.StatusEnum.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return info.isActive();
    }
}
