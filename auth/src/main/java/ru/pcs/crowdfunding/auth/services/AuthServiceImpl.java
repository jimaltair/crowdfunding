package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthenticationInfoDto;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.RoleRepository;
import ru.pcs.crowdfunding.auth.repositories.StatusRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
        return authenticationInfoDto;
    }

    @Override
    public Optional<AuthenticationInfo> createAuthenticationInfo(AuthenticationInfoDto authenticationInfo) {

        Optional<AuthenticationInfo> authInfo = authenticationInfosRepository.findByEmail(authenticationInfo.getEmail());

        if (authInfo.isPresent()) {
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

            System.out.println("AuthenticationInfo: "+ build);

            AuthenticationInfo info = authenticationInfosRepository.save(build);
            return Optional.of(info);
        }
    }

    @Override
    public Optional<AuthenticationInfoDto> updateAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfo) {
        return Optional.of(AuthenticationInfoDto.builder().build());
    }

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
            return Optional.of(AuthenticationInfoDto.from(byId));
        }
        return Optional.empty();
    }
}