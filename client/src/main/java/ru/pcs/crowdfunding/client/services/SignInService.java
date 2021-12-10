package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.AuthSignInRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignInResponse;
import ru.pcs.crowdfunding.client.dto.SignInForm;

public interface SignInService {
    AuthSignInResponse signIn(AuthSignInRequest authSignInRequest);
}
