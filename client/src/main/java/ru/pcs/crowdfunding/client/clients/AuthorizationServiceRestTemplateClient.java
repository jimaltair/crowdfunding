package ru.pcs.crowdfunding.client.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizationServiceRestTemplateClient implements AuthorizationServiceClient {

    private final static String URL = "http://localhost:8081";
    private final static String PING_URL = URL + "/api/v0/ping";

    private final RestTemplate restTemplate;

    @Autowired
    public AuthorizationServiceRestTemplateClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public ResponseEntity<String> ping() {
        return restTemplate.getForEntity(PING_URL, String.class);
    }
}
