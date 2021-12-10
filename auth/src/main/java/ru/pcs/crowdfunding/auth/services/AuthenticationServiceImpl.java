package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.RolesRepository;
import ru.pcs.crowdfunding.auth.repositories.StatusesRepository;
import ru.pcs.crowdfunding.auth.security.util.TokenContent;
import ru.pcs.crowdfunding.auth.security.util.TokenProvider;

import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class AuthenticationServiceImpl implements AuthenticationService {

    private final static Duration DEFAULT_TOKEN_DURATION = Duration.ofDays(30);

    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final AuthorizationInfosRepository authorizationInfosRepository;
    private final RolesRepository rolesRepository;
    private final StatusesRepository statusesRepository;

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
                .roles(Arrays.asList(rolesRepository.getByName(Role.RoleEnum.USER)))
                .status(statusesRepository.getByName(Status.StatusEnum.CONFIRMED))
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
    public AuthenticationInfoDto signInAuthentication(SignInForm client) {

        String email = client.getEmail();
        String password = client.getPassword();
        log.info("Search email - {} in 'authenticationInfosRepository", email);

        Optional<AuthenticationInfo> authenticationInfoOptional = authenticationInfosRepository
                .findByEmail(email);

        if (authenticationInfoOptional.isPresent()) {
            AuthenticationInfo authenticationInfoInRepo = authenticationInfoOptional.get();

            log.info("Check password - {} - in authenticationInfosRepository for email - {}", password, email);

            if (passwordEncoder.matches(password, authenticationInfoInRepo.getPassword())) {
                log.info("Password for email - {} - correct", email);
                Long userId = authenticationInfoInRepo.getUserId();

                log.info("Check existence accessToken into authorizationInfoRepository by id - {}", userId);
                String accessToken = authorizationInfosRepository.getById(userId).getAccessToken();

                if (accessToken == null) {
                    log.info("AccessToken had not existed by id {} and generated", userId);
                    accessToken = this.generateAccessToken(userId);
                }

                return AuthenticationInfoDto.builder().userId(userId).accessToken(accessToken).build();
            }
        }

        log.error("Password for email - {} - invalid", email);
        return AuthenticationInfoDto.builder().userId(0L).build(); //костыль пока
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
