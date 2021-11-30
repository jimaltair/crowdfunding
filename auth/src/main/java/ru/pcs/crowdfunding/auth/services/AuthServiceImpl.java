package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Override
    public Optional<AuthenticationInfoDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public AuthenticationInfoDto addAuthenticationInfo(AuthenticationInfoDto authenticationInfo) {
        return AuthenticationInfoDto.builder().build();
    }

    @Override
    public AuthenticationInfoDto updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo) {
        return AuthenticationInfoDto.builder().build();
    }

    @Override
    public AuthenticationInfoDto deleteAuthenticationInfo(Long id) {
        return AuthenticationInfoDto.builder().build();
    }
}
