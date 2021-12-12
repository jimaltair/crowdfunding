package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.RoleRepository;
import ru.pcs.crowdfunding.auth.repositories.StatusRepository;

import java.util.Arrays;
import java.util.Optional;

/**
 * В качестве прям придирок: лучше располагать аннотации в порядке увеличения длинны
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;

    @Override
    public Optional<AuthenticationInfoDto> findById(Long id) {
        Optional<AuthenticationInfoDto> authenticationInfoDto = authenticationInfosRepository.findById(id)
                .map(AuthenticationInfoDto::from);
        log.info("Result of 'findById' -  {} with 'authenticationInfoRepository'", authenticationInfoDto);
        return authenticationInfoDto;
    }

    /**
     * Возможно тут имеет смысл использовать @Retryable и @Transactional
     */
    @Override
    public Optional<AuthenticationInfo> createAuthenticationInfo(AuthenticationInfoDto authenticationInfo) {

        Optional<AuthenticationInfo> authInfo = authenticationInfosRepository.findByEmail(authenticationInfo.getEmail());

        if (authInfo.isPresent()) {
            log.error("An account with email: \"+ authenticationInfo.getEmail() + \" already exists");
            throw new IllegalArgumentException("An account with email: "+ authenticationInfo.getEmail() + " already exists.");
        } else {
            AuthenticationInfo build = AuthenticationInfo.builder()
                    .userId(authenticationInfo.getUserId())
                    .email(authenticationInfo.getEmail())
                    .password(authenticationInfo.getPassword())
                    .refreshToken("refresh")  //TODO дописать
                    .isActive(true)
                    .roles(Arrays.asList(roleRepository.getRoleByName("USER")))
                    .status(statusRepository.getStatusByName("CONFIRMED"))
                    .build();
            AuthenticationInfo result = authenticationInfosRepository.save(build);
            log.info("Result of 'createAuthenticationInfo' with 'authenticationInfosRepository' -  {}", result);
            return Optional.of(result);
        }
    }

    /** Возможно тут имеет смысл использовать @Retryable и @Transactional*/
    @Override
    public Optional<AuthenticationInfoDto> updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo) {
        Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.builder().build());
        log.info("Result of 'updateAuthenticationInfo' - {}", result);
        return result;
    }

    /** Возможно тут имеет смысл использовать @Retryable и @Transactional*/
    @Override
    public Optional<AuthenticationInfoDto> deleteAuthenticationInfo(Long id) {

        Optional<AuthenticationInfo> aut = authenticationInfosRepository.findById(id);
        if (aut.isPresent()) {
            AuthenticationInfo byId = AuthenticationInfo.builder()
                    .userId(aut.get().getUserId())
                    .email(aut.get().getEmail())
                    .isActive(false)
                    .password(aut.get().getPassword())
                    .refreshToken(aut.get().getRefreshToken())
                    .status(aut.get().getStatus())
                    .build();
            authenticationInfosRepository.save(byId);
            Optional<AuthenticationInfoDto> result = Optional.of(AuthenticationInfoDto.from(byId));
            log.info("Result of 'deleteAuthenticationInfo' - {}", result);
            return result;
        }
        Optional<AuthenticationInfoDto> result = Optional.empty();
        return result;
    }

}