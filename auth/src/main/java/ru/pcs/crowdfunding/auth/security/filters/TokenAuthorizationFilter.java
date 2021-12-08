package ru.pcs.crowdfunding.auth.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pcs.crowdfunding.auth.domain.Role;
import ru.pcs.crowdfunding.auth.domain.Status;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.security.config.SecurityConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final String JWT_SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader(AUTHORIZATION);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring("Bearer ".length());
            try {
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY)).build();
                DecodedJWT decodedJWT = verifier.verify(token);

                String roles = decodedJWT.getClaim("roles").asString();
                String status =  decodedJWT.getClaim("status").asString();
                log.info("Get {} token within authorization with roles {} and status: {}", token, roles, status);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(roles));


                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(token, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } catch (JWTVerificationException e) {
                logger.warn("Wrong token");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                objectMapper.writeValue(response.getWriter(),
                        Collections.singletonMap("error", "User not found with token"));
            }
        }
        else {
            logger.warn("Token is missing");
            filterChain.doFilter(request, response);
        }
    }

}
