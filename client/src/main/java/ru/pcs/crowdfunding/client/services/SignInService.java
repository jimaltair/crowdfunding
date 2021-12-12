package ru.pcs.crowdfunding.client.services;

import ru.pcs.crowdfunding.client.dto.AuthSignInRequest;
import ru.pcs.crowdfunding.client.dto.AuthSignInResponse;

public interface SignInService {

    /**
     * есть предложение использовать над каждым методом @Retryable(value = Exception.class)
     */
    AuthSignInResponse signIn(AuthSignInRequest authSignInRequest);

}