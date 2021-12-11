package ru.pcs.crowdfunding.client.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pcs.crowdfunding.client.security.CrowdfundingUtils;
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
        if (request.getRequestURI().equals(SecurityConfiguration.SIGN_IN_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.SIGN_UP_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.HOME_PAGE)) {
            filterChain.doFilter(request, response);
        } else {
            Optional<String> tokenOptional = CrowdfundingUtils.getCookieValueFromRequest(request, TOKEN_COOKIE_NAME);
            if (tokenOptional.isPresent()) {
                String token = tokenOptional.get();
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

                    // кладём id пользователя в аттрибуты RequestContextHolder'а, чтобы он был доступен из любой точки
                    // нашего сервиса
                    RequestContextHolder.getRequestAttributes().setAttribute("client_id", clientId, 1);

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
}
