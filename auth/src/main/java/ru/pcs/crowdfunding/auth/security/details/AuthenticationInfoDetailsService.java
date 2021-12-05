package ru.pcs.crowdfunding.auth.security.details;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;

@Service
@AllArgsConstructor
public class AuthenticationInfoDetailsService implements UserDetailsService {

    private final AuthenticationInfosRepository authenticationInfosRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AuthenticationInfoDetails(authenticationInfosRepository.
                findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Was not found")));
    }
}
