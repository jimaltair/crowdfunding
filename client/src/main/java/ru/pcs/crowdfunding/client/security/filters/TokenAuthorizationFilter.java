package ru.pcs.crowdfunding.client.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.services.ClientsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Order
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_COOKIE_NAME = "accessToken";

    private final JwtTokenProvider tokenProvider;

    private final ObjectMapper objectMapper;

    private final ClientsService clientsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(cracker -> cracker.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst();
        if (cookie.isPresent()) {
            String token = cookie.get().getValue();
            log.info("Got token {} from cookie", token);
            try {
                Long clientId = tokenProvider.getClientIdFromToken(token);
                log.info("Got client id={} from token", clientId);

                if (!clientsService.findById(clientId).isPresent()) {
                    logger.warn(String.format("Can't find user with id=%d", clientId));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", "user not found with token"));
                }
                //TODO: здесь можно добавить логику по дополнительной проверке роли юзера - её также можно достать из токена
                filterChain.doFilter(request, response);
            } catch (IllegalAccessException e) {
                logger.warn("Token is invalid");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                objectMapper.writeValue(response.getWriter(),
                        Collections.singletonMap("error", "Token is expired or invalid"));
            }
        } else {
            logger.warn("Token is missing");
            filterChain.doFilter(request, response);
        }
    }
}
