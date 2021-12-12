package ru.pcs.crowdfunding.client.security.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pcs.crowdfunding.client.security.SecurityConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenLogoutFilter extends OncePerRequestFilter {

    private final static RequestMatcher logoutRequestMatcher =
            new AntPathRequestMatcher("**/logout", "GET");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals(SecurityConfiguration.SIGN_IN_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.SIGN_UP_PAGE) ||
                request.getRequestURI().equals(SecurityConfiguration.HOME_PAGE)) {
            filterChain.doFilter(request, response);
        }
        else if (logoutRequestMatcher.matches(request)) {
            SecurityContextHolder.clearContext();
            return;
        }
    }

}
