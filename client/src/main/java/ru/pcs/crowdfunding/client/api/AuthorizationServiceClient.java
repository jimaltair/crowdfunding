package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.AuthSignUpRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignUpResponse;
import ru.pcs.crowdfunding.client.dto.GetAuthInfoResponseDto;

public interface AuthorizationServiceClient {
    AuthSignUpResponse signUp(AuthSignUpRequest request);

    GetAuthInfoResponseDto getAuthInfo(Long clientId);
}
