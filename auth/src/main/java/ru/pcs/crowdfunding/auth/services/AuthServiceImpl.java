package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationInfosRepository authenticationInfosRepository;

    @Override
    public Optional<AuthenticationInfoDto> findById(Long id) {
        Optional<AuthenticationInfoDto> result = authenticationInfosRepository.findById(id)
                .map(AuthenticationInfoDto::from);
        log.info("Result of 'findById' -  {} with 'authenticationInfoRepository'", result);
        return result;
    }

    @Override
    public AuthenticationInfoDto createAuthenticationInfo(AuthenticationInfoDto authenticationInfo) {
        Optional<AuthenticationInfo> authInfo = authenticationInfosRepository.findByEmail(authenticationInfo.getEmail());
        if (authInfo.isPresent()) {
            AuthenticationInfoDto result = AuthenticationInfoDto.builder()
                    .userId(authInfo.get().getUserId())
                    .email(authInfo.get().getEmail())
                    .password(authInfo.get().getPassword())
                    .isActive(authInfo.get().isActive())
                    .refreshToken(authInfo.get().getRefreshToken())
                    .build();
            log.info("Result of 'createAuthenticationInfo' with 'authenticationInfosRepository' -  {}", result);
            return result;
        } else {
            AuthenticationInfoDto result = AuthenticationInfoDto.builder().build();
            log.info("Result of 'createAuthenticationInfo' with 'authenticationInfosRepository' -  {}", result);
            return result;
        }
    }

    @Override
    public Optional<AuthenticationInfoDto> updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo) {
        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Result of 'updateAuthenticationInfo' - {}", result);
        return result;
    }

    @Override
    public Optional<AuthenticationInfoDto> deleteAuthenticationInfo(Long id) {
        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Result of 'deleteAuthenticationInfo' - {}", result);
        return result;
    }
}
