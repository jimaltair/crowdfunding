package ru.pcs.crowdfunding.client.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * В качестве прям придирок: лучше распологать аннотации в порядке увеличения длинны
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${security.jwt_secret_key}")
    private String JWT_SECRET_KEY;

    public Long getClientIdFromToken(String token) throws IllegalAccessException{
        Long clientId = null;

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            clientId = Long.valueOf(decodedJWT.getClaim("sub").asString());
        } catch (JWTVerificationException e) {
            log.warn("Wrong token");
            throw new IllegalAccessException(e.getMessage());
        }
        return clientId;
    }

}