package ru.pcs.crowdfunding.client.api;

import ru.pcs.crowdfunding.client.dto.*;

public interface AuthorizationServiceClient {
    AuthSignUpResponse signUp(AuthSignUpRequest request);

    AuthSignInResponse signIn(AuthSignInRequest request);

    GetAuthInfoResponseDto getAuthInfo(Long clientId);
}
