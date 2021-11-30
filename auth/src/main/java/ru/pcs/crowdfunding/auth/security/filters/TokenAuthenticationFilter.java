package ru.pcs.crowdfunding.auth.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.pcs.crowdfunding.auth.domain.AuthenticationInfo;
import ru.pcs.crowdfunding.auth.domain.AuthorizationInfo;
import ru.pcs.crowdfunding.auth.dto.SignInForm;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@Slf4j
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    private static final String JWT_SECRET_KEY = "jwt_secret";

    private final ObjectMapper objectMapper;
    private final AuthenticationInfosRepository authenticationInfosRepository;
    private final AuthorizationInfosRepository authorizationInfosRepository;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, AuthenticationInfosRepository authenticationInfosRepository, AuthorizationInfosRepository authorizationInfosRepository) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.authenticationInfosRepository = authenticationInfosRepository;
        this.authorizationInfosRepository = authorizationInfosRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignInForm form = objectMapper.readValue(request.getReader(), SignInForm.class);
            log.info("Attempt authentication by email {} and password {}", form.getEmail(), form.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(form.getEmail(),
                    form.getPassword());
            return super.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthenticationInfo authenticationInfo = (AuthenticationInfo) authResult.getPrincipal();
        AuthorizationInfo authorizationInfo = authorizationInfosRepository.getById(authenticationInfo.getUserId());

        String accessToken = JWT.create()
                .withSubject(authorizationInfo.getUserId().toString())
                .withClaim("role", authorizationInfo.getRole().toString())
                .withClaim("state",authorizationInfo.getStatus().toString())
                .withExpiresAt(new Date((System.currentTimeMillis()) + 20 * 60 * 1000))
                .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
        authenticationInfo.setAccessToken(accessToken);

        if (authenticationInfo.getRefreshToken() == null) {
            String refreshToken = JWT.create()
                    .withSubject(authenticationInfo.getUserId().toString())
                    .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
            authenticationInfo.setRefreshToken(refreshToken);
        }

        authenticationInfosRepository.save(authenticationInfo);
        authorizationInfosRepository.save(authorizationInfo);
    }
}

