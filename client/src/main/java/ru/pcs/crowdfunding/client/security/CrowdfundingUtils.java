package ru.pcs.crowdfunding.client.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class CrowdfundingUtils {

    public static Optional<Long> getClientIdFromRequestContext() {
        log.info("Try to get client id from RequestContextHolder");
        Optional<Long> clientId = Optional.ofNullable(
                (Long) RequestContextHolder.getRequestAttributes().getAttribute("client_id", 1));
        if (!clientId.isPresent()) {
            log.error("Can't get client id from RequestContextHolder");
        }
        return clientId;
    }

    public static Optional<String> getCookieValueFromRequest(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue);
    }
}
