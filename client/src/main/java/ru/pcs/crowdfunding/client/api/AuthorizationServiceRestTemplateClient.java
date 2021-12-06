package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignUpResponse;

@Component
public class AuthorizationServiceRestTemplateClient extends RestTemplateClient implements AuthorizationServiceClient {
    private static final String SIGNUP_URL = "/api/signUp";

    @Autowired
    public AuthorizationServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${api.authorization-service.remote-address}") String remoteAddress,
            ObjectMapper objectMapper) {
        super(restTemplateBuilder, remoteAddress, objectMapper);
    }

    @Override
    public AuthSignUpResponse signUp(AuthSignUpRequest request) {
        return post(SIGNUP_URL, request, AuthSignUpResponse.class);
    }
}
