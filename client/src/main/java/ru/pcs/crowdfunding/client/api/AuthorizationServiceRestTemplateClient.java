package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ru.pcs.crowdfunding.client.dto.*;

@Component
public class AuthorizationServiceRestTemplateClient extends RestTemplateClient implements AuthorizationServiceClient {
    private static final String SIGNUP_URL = "/api/signUp";
    private static final String SIGNIN_URL = "/api/signIn";
    private static final String GET_AUTH_URL = "/api/auth/{clientId}";


    @Autowired
    public AuthorizationServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${api.authorization-service.remote-address}") String remoteAddress,
            @Value ("${api.authorization-service.token}")String token,
            ObjectMapper objectMapper) {
        super(restTemplateBuilder, remoteAddress, token, objectMapper);
    }

    @Override
    public AuthSignUpResponse signUp(AuthSignUpRequest request) {
        return post(SIGNUP_URL, request, AuthSignUpResponse.class);
    }

    @Override
    public AuthSignInResponse signIn(AuthSignInRequest request) {
        return post(SIGNIN_URL, request, AuthSignInResponse.class);
    }

    @Override
    public GetAuthInfoResponseDto getAuthInfo(Long clientId) {
        return get(GET_AUTH_URL, GetAuthInfoResponseDto.class, clientId);
    }
}
