package ru.pcs.crowdfunding.auth.security.details;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.domain.Status;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthenticationInfoDetails implements UserDetails {

    private final AuthenticationInfo authenticationInfo;
    private final AuthorizationInfo authorizationInfo;

    public AuthenticationInfoDetails(AuthenticationInfo authenticationInfo, AuthorizationInfo authorizationInfo) {
        this.authenticationInfo = authenticationInfo;
        this.authorizationInfo = authorizationInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authenticationInfo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return authenticationInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return authenticationInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
//        return !authenticationInfo.getStatus().getName().equals(Status.StatusEnum.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
//        return authenticationInfo.getStatus().getName().equals(Status.StatusEnum.CONFIRMED);
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return authenticationInfo;
    }

    public AuthorizationInfo getAuthorizationInfo() {
        return authorizationInfo;
    }

    public Long getUserId() {
        return authenticationInfo.getUserId();
    }

    public void setAccessToken(String accessToken) {
        authorizationInfo.setAccessToken(accessToken);
    }

    public void setRefreshToken(String refreshToken) {
        authenticationInfo.setRefreshToken(refreshToken);
    }
}
