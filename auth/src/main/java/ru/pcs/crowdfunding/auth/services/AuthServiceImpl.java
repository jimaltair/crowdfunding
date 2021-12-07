package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Override
    public Optional<AuthenticationInfoDto> findById(Long id) {
        log.info("Запускается метод 'findById' с параметром 'id' - {}", id);

        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Результат выполения метода 'findById' - {}", result.get());

        return result;
    }

    @Override
    public AuthenticationInfoDto createAuthenticationInfo(AuthenticationInfoDto authenticationInfo) {
        Optional<AuthenticationInfo> authInfo = authenticationInfosRepository.findByEmail(authenticationInfo.getEmail());
        if (authInfo.isPresent()) {
            return AuthenticationInfoDto.builder()
                    .userId(authInfo.get().getUserId())
                    .email(authInfo.get().getEmail())
                    .password(authInfo.get().getPassword())
                    .isActive(authInfo.get().isActive())
                    .refreshToken(authInfo.get().getRefreshToken())
                    .build();
        } else {
            return AuthenticationInfoDto.builder().build();
        }
    }

    @Override
    public Optional<AuthenticationInfoDto> updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo) {
        log.info("Запускается метод 'updateAuthenticationInfo' с параметрами 'id' - {} и 'AuthenticationInfoDto' - {}"
                , id, authenticationInfo);

        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Результат выполнения метода 'updateAuthenticationInfo' - {}", result.get());

        return result;
    }

    @Override
    public Optional<AuthenticationInfoDto> deleteAuthenticationInfo(Long id) {
        log.info("Запускается метод 'deleteAuthenticationInfo' с параметром 'id' - {}", id);

        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Результат выполнения метода 'deleteAuthenticationInfo' - {}", result.get());

        return result;
    }
}
