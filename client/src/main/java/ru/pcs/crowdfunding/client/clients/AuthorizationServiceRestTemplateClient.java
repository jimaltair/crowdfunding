package ru.pcs.crowdfunding.client.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

@Component
public class AuthorizationServiceRestTemplateClient implements AuthorizationServiceClient {
    private static final String SIGNUP_URL = "/api/v0/signUp";

    private final RestTemplate restTemplate;
    private final String remoteAddress;

    @Autowired
    public AuthorizationServiceRestTemplateClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${clients.authorization-service.remote-address}") String remoteAddress) {
        this.restTemplate = restTemplateBuilder.build();
        this.remoteAddress = remoteAddress;
    }

    @Override
    public ResponseEntity<ResponseDto> signUp(AuthSignUpRequest request) {
        return restTemplate.postForEntity(remoteAddress + SIGNUP_URL, request, ResponseDto.class);
    }
}
