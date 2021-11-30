package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationInfosRepository authenticationInfosRepository;

    @Override
    public void signUpAuthentication(AuthenticationInfoDto client) {

        AuthenticationInfo clientInfo = AuthenticationInfo.builder()
                .email(client.getEmail().toLowerCase(Locale.ROOT))
                .password(client.getPassword())
                .userId(client.getUserId())
                .isActive(true)
//                .accessToken()
//                .refreshToken()
                .build();

        authenticationInfosRepository.save(clientInfo);
    }
}
