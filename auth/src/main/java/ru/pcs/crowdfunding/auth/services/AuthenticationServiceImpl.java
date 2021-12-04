package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationInfosRepository authenticationInfosRepository;

    @Override
    public AuthenticationInfoDto signUpAuthentication(AuthenticationInfoDto client) {

        AuthenticationInfo newClientInfo = AuthenticationInfo.builder()
                .email(client.getEmail().toLowerCase(Locale.ROOT))
                .password(client.getPassword())
                .userId(client.getUserId())
                .accessToken(client.getAccessToken())
                .refreshToken(client.getRefreshToken())
                .isActive(true)
                .build();
        authenticationInfosRepository.save(newClientInfo);

        return AuthenticationInfoDto.from(newClientInfo);
    }

    @Override
    public boolean existEmailInDb(AuthenticationInfoDto client) {
        return authenticationInfosRepository.findByEmail(client.getEmail()).isPresent();
    }
}
