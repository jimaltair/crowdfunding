package ru.pcs.crowdfunding.auth.services;

import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

import java.util.Optional;

public interface AuthService {

    Optional<AuthenticationInfoDto> findById(Long id);

    AuthenticationInfoDto addAuthenticationInfo(AuthenticationInfoDto authenticationInfo);

    AuthenticationInfoDto updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo);

    AuthenticationInfoDto deleteAuthenticationInfo(Long id);
}
