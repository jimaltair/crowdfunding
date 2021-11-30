package ru.pcs.crowdfunding.client.clients;

import org.springframework.http.ResponseEntity;
import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;

public interface AuthorizationServiceClient {
    ResponseEntity<String> ping();

    ResponseEntity<Void> signUp(AuthSignUpRequest request);
}
