package ru.pcs.crowdfunding.client.clients;

import org.springframework.http.ResponseEntity;

public interface AuthorizationServiceClient {
    ResponseEntity<String> ping();
}
