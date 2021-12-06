package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationInfosRepository authenticationInfosRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationInfoDto signUpAuthentication(AuthenticationInfoDto client) {
        log.info("Запускается метод 'signUpAuthentication' с параметрами 'AuthenticationInfoDto' - {}", client);

        AuthenticationInfo newClientInfo = AuthenticationInfo.builder()
                .email(client.getEmail().toLowerCase(Locale.ROOT))
                .password(passwordEncoder.encode(client.getPassword()))
                .userId(client.getUserId())
                .refreshToken(client.getRefreshToken())
                .isActive(true)
                .build();
        log.info("Создан новый 'AuthenticationInfo' - {}", newClientInfo);

        authenticationInfosRepository.save(newClientInfo);
        log.info("Завершен метод 'save' в 'authenticationInfosRepository' с параметром 'newClientInfo' - {}", newClientInfo);

        return AuthenticationInfoDto.from(newClientInfo);
    }

    @Override
    public boolean existEmailInDb(AuthenticationInfoDto client) {

        boolean isExistEmailInDb = authenticationInfosRepository.findByEmail(client.getEmail()).isPresent();
        log.info("Результат проверки существования email - {} в 'authenticationInfosRepository' = {}"
                , client.getEmail(), isExistEmailInDb);

        return isExistEmailInDb;
    }
}
