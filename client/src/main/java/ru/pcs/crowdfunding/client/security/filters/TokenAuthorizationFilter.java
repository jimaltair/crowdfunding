package ru.pcs.crowdfunding.client.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pcs.crowdfunding.client.security.CrowdfundingUtils;
import ru.pcs.crowdfunding.client.security.JwtTokenProvider;
import ru.pcs.crowdfunding.client.security.SecurityConfiguration;
import ru.pcs.crowdfunding.client.services.ClientsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Order
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_COOKIE_NAME = "accessToken";

    private static final RequestMatcher PROJECT_IMAGE_REQUEST_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.PROJECT_IMAGE_URL, "GET");
    private static final RequestMatcher CLIENT_IMAGE_REQUEST_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.CLIENT_IMAGE_URL, "GET");

    private static final RequestMatcher CSS_REQUEST_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.CSS_URL, "GET");
    private static final RequestMatcher JS_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.JS_URL, "GET");
    private static final RequestMatcher ICO_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.ICO_URL, "GET");
    private static final RequestMatcher JPG_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.JPG_URL, "GET");
    private static final RequestMatcher PNG_MATCHER =
            new AntPathRequestMatcher(SecurityConfiguration.PNG_URL, "GET");

    private final JwtTokenProvider tokenProvider;

    private final ObjectMapper objectMapper;

    private final ClientsService clientsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(SecurityConfiguration.SIGN_IN_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.SIGN_UP_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.HOME_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.LOGOUT_FILTER_PROCESSES_URL) ||
                isProjectOrClientImage(request) ||
                isStaticContent(request)) {
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
                    } else {

                        RequestContextHolder.getRequestAttributes().setAttribute("client_id", clientId, 1);
                        filterChain.doFilter(request, response);
                    }
                } catch (IllegalAccessException e) {
                    logger.warn("Token is invalid");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    objectMapper.writeValue(response.getWriter(),
                            Collections.singletonMap("error", "Token is expired or invalid"));
                }
            } else {
                logger.warn("Token is missing");
                response.sendRedirect("/signIn");
                filterChain.doFilter(request, response);
            }
        }
    }

    private boolean isProjectOrClientImage(HttpServletRequest request) {
        return PROJECT_IMAGE_REQUEST_MATCHER.matches(request) ||
                CLIENT_IMAGE_REQUEST_MATCHER.matches(request);
    }

    private boolean isStaticContent(HttpServletRequest request) {
        return CSS_REQUEST_MATCHER.matches(request) || JS_MATCHER.matches(request) ||
                ICO_MATCHER.matches(request) || JPG_MATCHER.matches(request) || PNG_MATCHER.matches(request);
    }
}
