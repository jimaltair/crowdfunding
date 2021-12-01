package ru.pcs.crowdfunding.auth.services;

import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

import java.util.Optional;

public interface AuthService {

    Optional<AuthenticationInfoDto> findById(Long id);

    AuthenticationInfoDto createAuthenticationInfo(AuthenticationInfoDto authenticationInfo);

    Optional<AuthenticationInfoDto> updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo);

    Optional<AuthenticationInfoDto> deleteAuthenticationInfo(Long id);
}
