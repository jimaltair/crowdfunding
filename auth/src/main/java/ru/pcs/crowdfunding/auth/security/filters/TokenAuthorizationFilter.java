package ru.pcs.crowdfunding.auth.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.homework33.repositories.AccountsRepository;
import com.example.homework33.security.config.SecurityConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final String JWT_SECRET_KEY = "jwt_secret";

    private final AccountsRepository accountsRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(SecurityConfiguration.LOGIN_FILTER_PROCESSES_URL) || request.getRequestURI().equals(SecurityConfiguration.REFRESH_FILTER_PROCESSES_URL + "/refresh")) {
            filterChain.doFilter(request, response);
        }
        else {
            String tokenHeader = request.getHeader(AUTHORIZATION);
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring("Bearer ".length());
                try {
                    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY)).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    //Обработка токена
                    String role = decodedJWT.getClaim("role").asString();
                    log.info("Get {} token within authorization with role {}", token, role);
                    Collection<SimpleGrantedAuthority> authorities = Collections.singleton
                            (new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(token, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                } catch (JWTVerificationException e) {
                    logger.warn("Wrong token");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    objectMapper.writeValue(response.getWriter(),
                            Collections.singletonMap("error", "user not found with token"));
                }
            }
            else {
                logger.warn("Token is missing");
                filterChain.doFilter(request, response);
            }
        }
    }
}
