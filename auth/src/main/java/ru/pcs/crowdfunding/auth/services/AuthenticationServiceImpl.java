package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.security.util.TokenContent;
import ru.pcs.crowdfunding.auth.security.util.TokenProvider;

import java.time.Duration;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {

    private final static Duration DEFAULT_TOKEN_DURATION = Duration.ofDays(30);

    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final AuthorizationInfosRepository authorizationInfosRepository;

    private final TokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthenticationInfoDto signUpAuthentication(AuthenticationInfoDto client) {

        AuthenticationInfo newClientInfo = AuthenticationInfo.builder()
                .email(client.getEmail().toLowerCase(Locale.ROOT))
                .password(passwordEncoder.encode(client.getPassword()))
                .userId(client.getUserId())
                .refreshToken(client.getRefreshToken())
                .isActive(true)
                .build();

        log.info("Saving 'newClientInfo' - {} in 'authenticationInfosRepository'", newClientInfo);
        authenticationInfosRepository.save(newClientInfo);

        AuthorizationInfo authorizationInfo = AuthorizationInfo.builder()
                .userId(client.getUserId())
                .accessToken(generateAccessToken(client.getUserId()))
                .build();
        log.info("Saving 'authorizationInfo' - {} in 'authorizationInfosRepostiory'", authorizationInfo);
        authorizationInfosRepository.save(authorizationInfo);

        AuthenticationInfoDto result = AuthenticationInfoDto.from(newClientInfo);
        result.setAccessToken(authorizationInfo.getAccessToken());
        log.info("Result of 'signUpAuthentication' - {}", result);
        return result;
    }

    @Override
    public boolean signInAuthentication(SignInForm client) {
        String email = client.getEmail();
        String password = client.getPassword();
        log.info("Search email - {} in 'authenticationInfosRepository", email);
        if (authenticationInfosRepository.findByEmail(email).isPresent()) {
            AuthenticationInfo authenticationInfoInRepo = authenticationInfosRepository
                    .findByEmail(email).get();

            log.info("Check password - {} - in authenticationInfosRepository for email - {}", password, email);
            password = passwordEncoder.encode(password); // кодируется иначе
            log.info("password encode - {}, correct password - {}", password, authenticationInfoInRepo.getPassword());
            if (passwordEncoder.encode(password).equals(authenticationInfoInRepo.getPassword())) {
                log.info("Password for email - {} - correct", email);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existEmailInDb(AuthenticationInfoDto client) {

        boolean isExistEmailInDb = authenticationInfosRepository.findByEmail(client.getEmail()).isPresent();
        log.info("Result of checking 'email' - {} in 'authenticationInfosRepository' = {}"
                , client.getEmail(), isExistEmailInDb);

        return isExistEmailInDb;
    }

    private String generateAccessToken(Long userId) {
        TokenContent tokenContent = TokenContent.builder()
                .userId(userId)
                .build();
        String result = tokenProvider.generate(tokenContent, DEFAULT_TOKEN_DURATION);
        log.info("Result of 'generateAccessToken' - {}", result);
        return result;
    }
}
