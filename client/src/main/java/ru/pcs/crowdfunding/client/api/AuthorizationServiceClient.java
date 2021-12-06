package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignUpResponse;

public interface AuthorizationServiceClient {
    AuthSignUpResponse signUp(AuthSignUpRequest request);
}
