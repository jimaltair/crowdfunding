package ru.pcs.crowdfunding.auth.security.util;

import java.time.Duration;

public interface TokenProvider {

    String generate(TokenContent tokenContent, Duration lifeTime);

    TokenContent decode(String token) throws Exception;

}