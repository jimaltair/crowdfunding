package ru.pcs.crowdfunding.client.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Order
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_COOKIE_NAME = "accessToken";

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(cracker -> cracker.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst();
        if (cookie.isPresent()) {
            String token = cookie.get().getValue();
            // происходит валидация токена
        } else {
            logger.warn("Token is missing");
            filterChain.doFilter(request, response);
        }
    }
}
