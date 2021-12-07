package ru.pcs.crowdfunding.auth.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.dto.AuthorizationInfoDto;
import ru.pcs.crowdfunding.auth.dto.ResponseDto;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.security.details.AuthenticationInfoDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String TOKEN = "token";
    private static final String JWT_SECRET_KEY = "jwt_secret_key";

    private final ObjectMapper objectMapper;
    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final AuthorizationInfosRepository authorizationInfosRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                     AuthenticationInfosRepository authenticationInfosRepository,
                                     AuthorizationInfosRepository authorizationInfosRepository,
                                     PasswordEncoder passwordEncoder) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.authenticationInfosRepository = authenticationInfosRepository;
        this.authorizationInfosRepository = authorizationInfosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            SignInForm form = objectMapper.readValue(request.getReader(), SignInForm.class);
            log.info("Attempt authentication by email {} and password {}", form.getEmail(), form.getPassword());

            // создать user
            if (!authenticationInfosRepository.findByEmail(form.getEmail()).isPresent()) {

                Role role = new Role(1L, Role.RoleEnum.valueOf(form.getRole()));
                Status status = new Status(2L, Status.StatusEnum.valueOf(form.getStatus()));

                AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                        .userId(form.getUserId())
                        .email(form.getEmail().toLowerCase(Locale.ROOT))
                        .password(passwordEncoder.encode(form.getPassword()))
                        .isActive(true)
                        .roles(Collections.singletonList(role))
                        .status(status)
                        .refreshToken("")
                        .build();
                authenticationInfosRepository.save(authenticationInfo);

                AuthorizationInfo authorizationInfo = AuthorizationInfo.builder()
                        .userId(form.getUserId())
                        .build();
                authorizationInfosRepository.save(authorizationInfo);
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    form.getEmail(), form.getPassword());
            return super.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        AuthenticationInfoDetails authenticationInfoDetails = (AuthenticationInfoDetails) authResult.getPrincipal();
        AuthenticationInfo authenticationInfo = authenticationInfoDetails.getAuthenticationInfo();
        AuthorizationInfo authorizationInfo = authenticationInfoDetails.getAuthorizationInfo();

        String accessToken = JWT.create()
                .withSubject(authenticationInfo.getUserId().toString())
                .withClaim("roles", authenticationInfo.getRoles().toString())
                .withClaim("status", authenticationInfo.getStatus().getName().toString())
                .withExpiresAt(new Date((System.currentTimeMillis()) + 2629800000L))
                .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
        authorizationInfo.setAccessToken(accessToken);

        String refreshToken = JWT.create()
                .withSubject(authenticationInfo.getUserId().toString())
                .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
        authenticationInfo.setRefreshToken(refreshToken);
        log.info("Created access token {} and refresh token {} ", accessToken, refreshToken);

        authenticationInfosRepository.save(authenticationInfo);
        authorizationInfosRepository.save(authorizationInfo);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        List<Role> roles = authenticationInfo.getRoles();
        AuthorizationInfoDto authorizationInfoDto = AuthorizationInfoDto.builder()
                .userId(authorizationInfo.getUserId())
                .role(roles.get(0).getName())
                .status(authenticationInfo.getStatus().getName())
                .accessToken(authorizationInfo.getAccessToken())
                .build();
        objectMapper.writeValue(response.getWriter(),
                ResponseDto.builder().data(authorizationInfoDto).success(true).build());

    }
}
