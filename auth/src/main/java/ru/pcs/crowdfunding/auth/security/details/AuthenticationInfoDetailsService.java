package ru.pcs.crowdfunding.auth.security.details;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;

@RequiredArgsConstructor
@Service
public class AuthenticationInfoDetailsService implements UserDetailsService {

    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final AuthorizationInfosRepository authorizationInfosRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (authenticationInfosRepository.findByEmail(email).isPresent()) {
            return new AuthenticationInfoDetails(authenticationInfosRepository.findByEmail(email).get(),
                    authorizationInfosRepository.findByUserId(
                            authenticationInfosRepository.findByEmail(email).get().getUserId()).get());
        } else {
            return new AuthenticationInfoDetails(authenticationInfosRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Was not found")),
                    authorizationInfosRepository.findByUserId(
                            authenticationInfosRepository.findByEmail(email).get().getUserId()).get());
        }
    }
}
